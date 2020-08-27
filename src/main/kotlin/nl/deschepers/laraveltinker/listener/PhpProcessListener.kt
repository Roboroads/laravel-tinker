package nl.deschepers.laraveltinker.listener

import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.util.Key

class PhpProcessListener : ProcessListener {
    val processOutput = ArrayList<String>()
    private var firstLine = true

    @Suppress("EmptyFunctionBlock")
    override fun startNotified(event: ProcessEvent) {
    }

    @Suppress("EmptyFunctionBlock")
    override fun processTerminated(event: ProcessEvent) {
    }

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {
        if (firstLine) {
            firstLine = false
            return
        }

        processOutput.add(event.text)
    }
}
