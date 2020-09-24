package nl.deschepers.laraveltinker.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowManager
import com.jetbrains.php.composer.ComposerUtils
import com.jetbrains.php.config.PhpProjectConfigurationFacade
import com.jetbrains.php.config.commandLine.PhpCommandSettings
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder
import com.jetbrains.php.run.PhpEditInterpreterExecutionException
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import com.jetbrains.php.run.script.PhpScriptRuntimeConfigurationProducer
import nl.deschepers.laraveltinker.LaravelTinkerBundle
import nl.deschepers.laraveltinker.balloon.NoPhpInterpreterBalloon
import nl.deschepers.laraveltinker.balloon.PhpInterpreterErrorBalloon
import nl.deschepers.laraveltinker.cache.PersistentProjectCache
import nl.deschepers.laraveltinker.listener.PhpProcessListener
import nl.deschepers.laraveltinker.toolwindow.TinkerOutputToolWindowFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class PhpArtisanTinker(private val project: Project, private val phpCode: String) {
    fun run() {
        FileDocumentManager.getInstance().saveAllDocuments()

        val runConfiguration = PhpScriptRunConfiguration(
            project,
            PhpScriptRuntimeConfigurationProducer().configurationFactory,
            "Laravel Tinker"
        )

        val phpInterpreter = PhpProjectConfigurationFacade.getInstance(project).interpreter
        if (phpInterpreter == null) {
            NoPhpInterpreterBalloon(project).show()
            return
        }

        val composerDirPath = ComposerUtils.findFileInProject(
            project,
            ComposerUtils.CONFIG_DEFAULT_FILENAME
        ).parent.path

        val inputStream = javaClass.classLoader.getResourceAsStream("scripts/tinker_run.php")
        val phpTinkerCodeRunnerCode = BufferedReader(
            InputStreamReader(inputStream!!, StandardCharsets.UTF_8)
        ).lines().collect(Collectors.joining("\n"))
        val phpCommandSettings: PhpCommandSettings
        val processHandler: ProcessHandler

        try {
            phpCommandSettings = PhpCommandSettingsBuilder(project, phpInterpreter).build()
            phpCommandSettings.setWorkingDir(composerDirPath)
            phpCommandSettings.importCommandLineSettings(
                runConfiguration.settings.commandLineSettings,
                composerDirPath
            )
            phpCommandSettings.addArguments(
                listOf(
                    "-r",
                    phpTinkerCodeRunnerCode,
                    phpCode
                )
            )

            processHandler = runConfiguration.createProcessHandler(project, phpCommandSettings)
            ProcessTerminatedListener.attach(
                processHandler,
                project,
                ""
            )
        } catch (ex: ExecutionException) {
            PhpInterpreterErrorBalloon(
                project,
                ex.message ?: LaravelTinkerBundle.message("lt.error.php.interpreter.error")
            ).show()

            return
        } catch (ex: PhpEditInterpreterExecutionException) {
            PhpInterpreterErrorBalloon(
                project,
                ex.message ?: LaravelTinkerBundle.message("lt.error.php.interpreter.error")
            ).show()

            return
        }

        val phpProcessListener = PhpProcessListener(processHandler)
        processHandler.addProcessListener(phpProcessListener)

        ToolWindowManager.getInstance(project).getToolWindow("Laravel Tinker")?.activate(null)
        TinkerOutputToolWindowFactory.tinkerOutputToolWindow?.resetOutput()

        project.getService(PersistentProjectCache::class.java).state.lastCode = phpCode

        ProgressManager.getInstance().run(
            object : Backgroundable(
                project,
                LaravelTinkerBundle.message("lt.running")
            ) {
                override fun run(progressIndicator: ProgressIndicator) {
                    processHandler.startNotify()
                    processHandler.processInput?.writer()?.write("\u0004")
                    while (!processHandler.isProcessTerminated) {
                        Thread.sleep(250)
                        try {
                            progressIndicator.checkCanceled()
                        } catch (ex: ProcessCanceledException) {
                            processHandler.waitFor(5000)
                            throw ex
                        }
                    }
                }
            }
        )
    }
}
