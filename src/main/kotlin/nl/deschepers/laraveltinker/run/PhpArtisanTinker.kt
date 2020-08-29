package nl.deschepers.laraveltinker.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.jetbrains.php.composer.ComposerUtils
import com.jetbrains.php.config.PhpProjectConfigurationFacade
import com.jetbrains.php.config.commandLine.PhpCommandSettings
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import com.jetbrains.php.run.script.PhpScriptRuntimeConfigurationProducer
import nl.deschepers.laraveltinker.LaravelTinkerBundle
import nl.deschepers.laraveltinker.balloon.NoPhpInterpreterBalloon
import nl.deschepers.laraveltinker.balloon.PhpInterpreterErrorBalloon
import nl.deschepers.laraveltinker.balloon.ProcessWaitErrorBalloon
import nl.deschepers.laraveltinker.listener.PhpProcessListener
import nl.deschepers.laraveltinker.toolwindow.TinkerOutputToolWindowFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

class PhpArtisanTinker(private val project: Project, private val phpCode: String) {
    fun run() {
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

        val inputStream = javaClass.classLoader.getResourceAsStream("scripts/tinker_run.command")
        val phpTinkerCodeRunnerCode = BufferedReader(
            InputStreamReader(inputStream!!, StandardCharsets.UTF_8)
        ).lines().collect(Collectors.joining("\n"))
        val phpCommandSettings: PhpCommandSettings

        try {
            phpCommandSettings = PhpCommandSettingsBuilder(project, phpInterpreter).build()
            phpCommandSettings.setWorkingDir(composerDirPath)
            phpCommandSettings.importCommandLineSettings(
                runConfiguration.settings.commandLineSettings,
                composerDirPath
            )
            phpCommandSettings.addArguments(listOf("-r", phpTinkerCodeRunnerCode, phpCode))
        } catch (ex: ExecutionException) {
            PhpInterpreterErrorBalloon(
                project,
                ex.message ?: LaravelTinkerBundle.message("lt.unknown.error")
            ).show()

            return
        }

        val processHandler = runConfiguration.createProcessHandler(project, phpCommandSettings)
        ProcessTerminatedListener.attach(
            processHandler,
            project,
            LaravelTinkerBundle.message("lt.execution.finished")
        )

        val phpProcessListener = PhpProcessListener()
        processHandler.addProcessListener(phpProcessListener)

        TinkerOutputToolWindowFactory.tinkerOutputToolWindow?.resetOutput()

        ProgressManager.getInstance().run(
            object : Backgroundable(
                project,
                LaravelTinkerBundle.message("lt.running")
            ) {
                override fun run(progressIndicator: ProgressIndicator) {
                    processHandler.startNotify()
                    if (!processHandler.waitFor()) {
                        ProcessWaitErrorBalloon(project).show()
                    }
                }
            }
        )
    }
}
