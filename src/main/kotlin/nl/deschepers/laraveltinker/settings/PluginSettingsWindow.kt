package nl.deschepers.laraveltinker.settings

import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import nl.deschepers.laraveltinker.Strings
import javax.swing.JPanel

class PluginSettingsWindow {
    private var settingsPanel: JPanel
    private val showExecutionStartedCheckbox = JBCheckBox(Strings.get("lt.setting.show_execution_start"))
    private val showExecutionEndedCheckbox = JBCheckBox(Strings.get("lt.setting.show_execution_end"))
    private val useWordWrappingCheckbox = JBCheckBox(Strings.get("lt.setting.use_word_wrapping"))

    var showExecutionStarted: Boolean
        get() = this.showExecutionStartedCheckbox.isSelected
        set(value) {
            this.showExecutionStartedCheckbox.isSelected = value
        }
    var showExecutionEnded: Boolean
        get() = this.showExecutionEndedCheckbox.isSelected
        set(value) {
            this.showExecutionEndedCheckbox.isSelected = value
        }
    var useWordWrapping: Boolean
        get() = this.useWordWrappingCheckbox.isSelected
        set(value) {
            this.useWordWrappingCheckbox.isSelected = value
        }

    init {
        settingsPanel = FormBuilder.createFormBuilder()
            .addComponent(showExecutionStartedCheckbox)
            .addComponent(showExecutionEndedCheckbox)
            .addComponent(useWordWrappingCheckbox)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getPanel(): JPanel {
        return settingsPanel
    }
}
