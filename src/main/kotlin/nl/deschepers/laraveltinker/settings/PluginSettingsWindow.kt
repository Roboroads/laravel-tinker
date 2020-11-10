package nl.deschepers.laraveltinker.settings

import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import org.jetbrains.annotations.NotNull
import javax.swing.JPanel

class PluginSettingsWindow {
    private var settingsPanel: JPanel
    private val commandLineParams = JBTextField()

    init {
        settingsPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel("Extra command line params: "),
                commandLineParams,
                1,
                true
            )
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getPanel(): JPanel {
        return settingsPanel
    }

    fun getCommandLineParams(): String {
        return commandLineParams.text
    }

    fun setCommandLineParams(newText: @NotNull String) {
        commandLineParams.text = newText
    }
}
