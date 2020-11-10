package nl.deschepers.laraveltinker.settings

import com.intellij.ui.components.JBCheckBox
import com.intellij.util.ui.FormBuilder
import nl.deschepers.laraveltinker.LaravelTinkerBundle
import javax.swing.JPanel

class PluginSettingsWindow {
    private var settingsPanel: JPanel
    private val showExecutionStartedCheckbox = JBCheckBox(LaravelTinkerBundle.message("lt.setting.show_execution_start"))
    private val showExecutionEndedCheckbox = JBCheckBox(LaravelTinkerBundle.message("lt.setting.show_execution_end"))

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

    init {
        settingsPanel = FormBuilder.createFormBuilder()
            .addComponent(showExecutionStartedCheckbox)
            .addComponent(showExecutionEndedCheckbox)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getPanel(): JPanel {
        return settingsPanel
    }
}
