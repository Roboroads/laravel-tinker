package nl.deschepers.laraveltinker.util

import com.intellij.execution.ExecutionException
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
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
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.balloon.LaravelRootDoesNotExistBalloon
import nl.deschepers.laraveltinker.balloon.VendorFolderNotFound
import nl.deschepers.laraveltinker.balloon.NoPhpInterpreterBalloon
import nl.deschepers.laraveltinker.balloon.PhpInterpreterErrorBalloon
import nl.deschepers.laraveltinker.listener.PhpProcessListener
import nl.deschepers.laraveltinker.settings.ProjectSettingsState
import nl.deschepers.laraveltinker.toolwindow.TinkerOutputToolWindowFactory

class PhpArtisanTinkerUtil(private val project: Project, private val phpCode: String) {
    fun run() {
        val projectSettings = ProjectSettingsState.getInstance(project)
        FileDocumentManager.getInstance().saveAllDocuments()

        val runConfiguration =
            PhpScriptRunConfiguration(
                project,
                PhpScriptRuntimeConfigurationProducer().configurationFactory,
                "Laravel Tinker"
            )

        val phpInterpreter = PhpProjectConfigurationFacade.getInstance(project).interpreter
        if (phpInterpreter == null) {
            NoPhpInterpreterBalloon(project).show()
            return
        }

        var laravelRoot =
            ComposerUtils.findFileInProject(project, ComposerUtils.CONFIG_DEFAULT_FILENAME)
                .parent
                .path

        // If the user has set a custom path, use that instead
        if (projectSettings.laravelRoot.isNotEmpty()) {
            val customLaravelRoot = File(projectSettings.laravelRoot + "/bootstrap/app.php")
            if (customLaravelRoot.exists() && customLaravelRoot.isFile) {
                laravelRoot = projectSettings.laravelRoot
            } else {
                LaravelRootDoesNotExistBalloon(project, projectSettings.laravelRoot).show()
                return
            }
        }

        // Check if the vendor path exists
        var vendorPath = projectSettings.vendorRoot.ifEmpty { laravelRoot }
        if(!vendorPath.endsWith("/vendor")) {
            vendorPath = "$vendorPath/vendor"
        }
        val vendorDir = File(vendorPath)
        if (!vendorDir.exists() || !vendorDir.isDirectory) {
            VendorFolderNotFound(project, vendorPath).show()
            return
        }

        val inputStream = javaClass.classLoader.getResourceAsStream("scripts/tinker_run.php")
        val phpTinkerCodeRunnerCode =
            BufferedReader(InputStreamReader(inputStream!!, StandardCharsets.UTF_8)).lines()
                .collect(Collectors.joining("\n"))
        val phpCommandSettings: PhpCommandSettings
        val processHandler: ProcessHandler

        try {
            phpCommandSettings =
                PhpCommandSettingsBuilder(project, phpInterpreter).loadAndStartDebug().build()
            phpCommandSettings.setWorkingDir(laravelRoot)
            phpCommandSettings.importCommandLineSettings(
                runConfiguration.settings.commandLineSettings,
                laravelRoot
            )

            val tinkerRunSettings = projectSettings.parseJson()
            tinkerRunSettings.addProperty("keywordColor", HelperUtil.colorToHex(DefaultLanguageHighlighterColors.KEYWORD.defaultAttributes.foregroundColor))
            tinkerRunSettings.addProperty("intColor", HelperUtil.colorToHex(DefaultLanguageHighlighterColors.NUMBER.defaultAttributes.foregroundColor))
            tinkerRunSettings.addProperty("stringColor", HelperUtil.colorToHex(DefaultLanguageHighlighterColors.STRING.defaultAttributes.foregroundColor))
            tinkerRunSettings.addProperty("floatColor", HelperUtil.colorToHex(DefaultLanguageHighlighterColors.NUMBER.defaultAttributes.foregroundColor))

            phpCommandSettings.addArguments(listOf("-r", phpTinkerCodeRunnerCode, phpCode, tinkerRunSettings.toString()))

            processHandler = runConfiguration.createProcessHandler(project, phpCommandSettings)
            ProcessTerminatedListener.attach(processHandler, project, "")
        } catch (ex: ExecutionException) {
            PhpInterpreterErrorBalloon(
                project,
                ex.message ?: Strings.get("lt.error.php_interpreter_error")
            ).show()

            return
        } catch (ex: PhpEditInterpreterExecutionException) {
            PhpInterpreterErrorBalloon(
                project,
                ex.message ?: Strings.get("lt.error.php_interpreter_error")
            ).show()

            return
        }

        val phpProcessListener = PhpProcessListener(project, processHandler)
        processHandler.addProcessListener(phpProcessListener)

        ToolWindowManager.getInstance(project).getToolWindow("Laravel Tinker")?.activate(null)
        TinkerOutputToolWindowFactory.tinkerOutputToolWindow[project]?.resetOutput()

        ProgressManager.getInstance()
            .run(
                object : Backgroundable(project, Strings.get("lt.running")) {
                    override fun run(progressIndicator: ProgressIndicator) {
                        processHandler.startNotify()
                        processHandler.processInput?.writer()?.write("\u0004")
                        while (!processHandler.isProcessTerminated) {
                            Thread.sleep(250)
                            try {
                                progressIndicator.checkCanceled()
                            } catch (ex: ProcessCanceledException) {
                                processHandler.destroyProcess()
                                throw ex
                            }
                        }
                    }
                }
            )
    }
}
