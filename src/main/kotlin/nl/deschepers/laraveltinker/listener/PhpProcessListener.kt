package nl.deschepers.laraveltinker.listener

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.settings.GlobalSettingsState
import nl.deschepers.laraveltinker.toolwindow.TinkerOutputToolWindowFactory
import nl.deschepers.laraveltinker.util.PlugUtil
import java.util.*

class PhpProcessListener(private val project: Project) :
    ProcessListener {

    companion object {
        private const val OUTPUT_START_SEQUENCE = "%%START-OUTPUT%%"
        private const val OUTPUT_END_SEQUENCE = "%%END-OUTPUT%%"
    }

    private val processOutput = ArrayList<String>()

    private var capturing = false
    private var firstLine = true

    override fun startNotified(event: ProcessEvent) {
        TinkerOutputToolWindowFactory.tinkerOutputToolWindow[project]?.plug = null
    }

    override fun processTerminated(event: ProcessEvent) {
        ApplicationManager.getApplication()
            .invokeLater(
                {
                    val pluginSettings = GlobalSettingsState.getInstance()

                    if (pluginSettings.showExecutionEnded) {
                        TinkerOutputToolWindowFactory.tinkerOutputToolWindow[project]
                            ?.addOutput("\n\n" + Strings.get("lt.execution_finished"))
                    }

                    TinkerOutputToolWindowFactory.tinkerOutputToolWindow[project]?.plug = PlugUtil.getPlug()
                },
                ModalityState.nonModal()
            )
    }

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        val capText = event.text.trim()
        //print(capText) // DEV: Uncomment to see what the output is that comes from the PHP process.

        // try decoding base64
        try {
            val text = String(Base64.getDecoder().decode(capText))
            processOutput.add(text)
            ApplicationManager.getApplication()
                .invokeLater(
                    {
                        TinkerOutputToolWindowFactory.tinkerOutputToolWindow[project]
                            ?.addOutput(text)
                    },
                    ModalityState.nonModal()
                )
        } catch (e: Exception) {
            // do nothing, not tinker output.
        }
    }
}
