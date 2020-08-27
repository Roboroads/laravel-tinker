package nl.deschepers.laraveltinker.run

import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.jetbrains.php.composer.ComposerUtils
import com.jetbrains.php.config.PhpProjectConfigurationFacade
import com.jetbrains.php.config.commandLine.PhpCommandSettingsBuilder
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import com.jetbrains.php.run.script.PhpScriptRuntimeConfigurationProducer
import nl.deschepers.laraveltinker.listener.PhpProcessListener
import nl.deschepers.laraveltinker.toolwindow.TinkerOutputToolWindowFactory
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets


class PhpArtisanTinker(private val project: Project, private val phpCode: String) {
    fun run() {
        try {
            val runConfiguration = PhpScriptRunConfiguration(
                    project,
                    PhpScriptRuntimeConfigurationProducer().configurationFactory,
                    "Laravel Tinker"
            );

            val phpInterpreter = PhpProjectConfigurationFacade.getInstance(project).interpreter
            if (phpInterpreter == null || phpInterpreter.pathToPhpExecutable == null) {
                Notifications.Bus.notify(NotificationGroup.Companion.balloonGroup("PHP Interpreter error").createNotification("PHP Interpreter error", NotificationType.ERROR), project)
                return
            }

            val composerDirPath = ComposerUtils.findFileInProject(project, ComposerUtils.CONFIG_DEFAULT_FILENAME).parent.path

            val inputStream = javaClass.classLoader.getResourceAsStream("scripts/tinker_run.command")
            val phpTinkerCodeRunnerCode = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

            val phpCommandSettings = PhpCommandSettingsBuilder(project, phpInterpreter).build()
            phpCommandSettings.setWorkingDir(composerDirPath)
            phpCommandSettings.importCommandLineSettings(runConfiguration.settings.commandLineSettings, composerDirPath)
            phpCommandSettings.addArguments(listOf("-r", phpTinkerCodeRunnerCode, phpCode))

            val processHandler = runConfiguration.createProcessHandler(project, phpCommandSettings)
            ProcessTerminatedListener.attach(processHandler, project, "Laravel Tinker execution finished.")

            val phpProcessListener = PhpProcessListener()
            processHandler.addProcessListener(phpProcessListener)

            ProgressManager.getInstance().run(object : Backgroundable(project, "Running Tinker") {
                override fun run(progressIndicator: ProgressIndicator) {
                    processHandler.startNotify()
                    if (!processHandler.waitFor()) {
                        Notifications.Bus.notify(NotificationGroup.Companion.balloonGroup("Could not wait for process finish").createNotification("Could not wait for process finish", NotificationType.ERROR), project)
                    } else {
                        val output = phpProcessListener.processOutput;
                        output.removeAt(output.size - 1)
                        System.out.println(output);
                        ApplicationManager.getApplication().invokeLater({
                            TinkerOutputToolWindowFactory.tinkerOutputToolWindow?.setTinkerOutput(output)
                        }, ModalityState.NON_MODAL)
                    }
                }
            })
        } catch (ex: Exception) {
            Notifications.Bus.notify(NotificationGroup.Companion.balloonGroup("Could not run tinker").createNotification("Could not run tinker", NotificationType.ERROR), project)
        }

    }
}