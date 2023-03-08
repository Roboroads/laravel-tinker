package nl.deschepers.laraveltinker.util

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.encoding.EncodingProjectManager
import com.jetbrains.php.config.commandLine.PhpCommandSettings
import com.jetbrains.php.run.PhpEditInterpreterExecutionException
import com.jetbrains.php.run.PhpExternalExecutionException
import com.jetbrains.php.run.remote.PhpRemoteInterpreterManager
import com.jetbrains.php.run.script.PhpScriptRunConfiguration
import java.nio.charset.Charset

class TinkerConsoleRunConfiguration(project: Project?, factory: ConfigurationFactory?, name: String?) : PhpScriptRunConfiguration(project, factory, name) {

    override fun createProcessHandler(project: Project, command: PhpCommandSettings, withPty: Boolean, optionalCommandLine: GeneralCommandLine?): ProcessHandler {
        return try {
            command.validateAndNotify(project)
            val commandLine = optionalCommandLine ?: command.createGeneralCommandLine(withPty)
            if (command.isRemote) {
                val manager = PhpRemoteInterpreterManager.getInstance()
                manager?.getRemoteProcessHandler(project, command.additionalData!!, commandLine, showVerboseOutput(), *command.additionalMappings)
                    ?: throw ExecutionException(PhpRemoteInterpreterManager.getRemoteInterpreterPluginIsDisabledErrorMessage())
            } else {
                object : KillableProcessHandler(commandLine) {
                    override fun getCharset(): Charset {
                        return EncodingProjectManager.getInstance(project).defaultCharset
                    }
                }
            }
        } catch (ex: PhpExternalExecutionException) {
            throw ex
        } catch (ex: InterruptedException) {
            throw PhpEditInterpreterExecutionException(project, ex.message!!, ex)
        } catch (ex: ExecutionException) {
            throw PhpEditInterpreterExecutionException(project, ex.message!!, ex)
        }
    }
}
