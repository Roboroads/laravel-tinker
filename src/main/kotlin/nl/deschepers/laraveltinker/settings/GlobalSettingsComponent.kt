package nl.deschepers.laraveltinker.settings

import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import nl.deschepers.laraveltinker.Strings
import javax.swing.JPanel
import javax.swing.JTextField

class GlobalSettingsComponent {
    private var settingsPanel: JPanel
    private val showExecutionStartedCheckbox = JBCheckBox(Strings.get("lt.setting.show_execution_start"))
    private val showExecutionEndedCheckbox = JBCheckBox(Strings.get("lt.setting.show_execution_end"))
    private val useWordWrappingCheckbox = JBCheckBox(Strings.get("lt.setting.use_word_wrapping"))
    private val patreonKeyTextField = JTextField()

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
    var patreonKey: String
        get() = this.patreonKeyTextField.text
        set(value) {
            this.patreonKeyTextField.text = value
        }

    init {
        val outputSettingsPanel = FormBuilder.createFormBuilder()
            .addComponent(showExecutionStartedCheckbox)
            .addComponent(showExecutionEndedCheckbox)
            .addComponent(useWordWrappingCheckbox)
            .addComponentFillVertically(JPanel(), 0)
            .panel
        outputSettingsPanel.border = IdeBorderFactory.createTitledBorder(Strings.get("lt.settings.output_settings"))

        val otherSettingsPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel(Strings.get("lt.setting.patreon_key")), patreonKeyTextField)
            .addTooltip(Strings.get("lt.setting.patreon_key.tooltip"))
            .addTooltip(Strings.get("lt.setting.patreon_key.tooltip_patreon"))
            .addComponentFillVertically(JPanel(), 0)
            .panel
        otherSettingsPanel.border = IdeBorderFactory.createTitledBorder(Strings.get("lt.settings.other_settings"))

        settingsPanel = FormBuilder.createFormBuilder()
            .addComponent(outputSettingsPanel)
            .addComponent(otherSettingsPanel)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    fun getPanel(): JPanel {
        return settingsPanel
    }
}
