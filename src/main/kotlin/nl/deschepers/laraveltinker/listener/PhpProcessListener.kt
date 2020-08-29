package nl.deschepers.laraveltinker.listener

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.util.Key
import nl.deschepers.laraveltinker.toolwindow.TinkerOutputToolWindowFactory

class PhpProcessListener : ProcessListener {
    companion object {
        private const val OUTPUT_START_SEQUENCE = "%%START-OUTPUT%%"
        private const val OUTPUT_END_SEQUENCE = "%%END-OUTPUT%%"
    }

    val processOutput = ArrayList<String>()

    private var capturing = false
    private var firstLine = true

    @Suppress("EmptyFunctionBlock")
    override fun startNotified(event: ProcessEvent) {
    }

    override fun processTerminated(event: ProcessEvent) {
        ApplicationManager.getApplication().invokeLater(
            {
                TinkerOutputToolWindowFactory
                    .tinkerOutputToolWindow?.addOutput("\n\n*** Execution finished ***")
            },
            ModalityState.NON_MODAL
        )
    }

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        if (firstLine) {
            firstLine = false
            return
        }

        var capText = event.text

        if (!capturing && capText.contains(OUTPUT_START_SEQUENCE)) {
            capText = capText.substring(
                capText.indexOf(OUTPUT_START_SEQUENCE) + OUTPUT_START_SEQUENCE.length
            )
            capturing = true
        }
        if (capturing && capText.contains(OUTPUT_END_SEQUENCE)) {
            capText = capText.substring(0, capText.indexOf(OUTPUT_END_SEQUENCE))
            processOutput.add(capText)
            capturing = false
        }

        if (capturing) {
            processOutput.add(capText)
            ApplicationManager.getApplication().invokeLater(
                { TinkerOutputToolWindowFactory.tinkerOutputToolWindow?.addOutput(capText) },
                ModalityState.NON_MODAL
            )
        }
    }
}
