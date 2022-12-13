package nl.deschepers.laraveltinker.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.FormBuilder
import nl.deschepers.laraveltinker.Strings
import javax.swing.JPanel

class ProjectSettingsComponent {
    private var settingsPanel: JPanel
    private val laravelRootTextField = TextFieldWithBrowseButton()
    private val vendorRootTextField = TextFieldWithBrowseButton()
    private val terminateAppCheckBox = JBCheckBox(Strings.get("lt.settings.terminate_app"))

    var laravelRoot: String
        get() = this.laravelRootTextField.text
        set(value) {
            this.laravelRootTextField.text = value
        }
    var vendorRoot: String
        get() = this.vendorRootTextField.text
        set(value) {
            this.vendorRootTextField.text = value
        }
    var terminateApp: Boolean
        get() = this.terminateAppCheckBox.isSelected
        set(value) {
            this.terminateAppCheckBox.isSelected = value
        }

    init {
        laravelRootTextField.addBrowseFolderListener(
            TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFolderDescriptor())
        )

        vendorRootTextField.addBrowseFolderListener(
            TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFolderDescriptor())
        )

        val projectSettingsPanel =
            FormBuilder.createFormBuilder()
                .addLabeledComponent(
                    JBLabel(Strings.get("lt.settings.laravel_root")),
                    laravelRootTextField
                )
                .addLabeledComponent(
                    JBLabel(Strings.get("lt.settings.vendor_root")),
                    vendorRootTextField
                )
                .addTooltip(Strings.get("lt.settings.laravel_root.tooltip1"))
                .addTooltip(Strings.get("lt.settings.laravel_root.tooltip2"))
                .addTooltip(Strings.get("lt.settings.laravel_root.tooltip3"))
                .addComponent(terminateAppCheckBox)
                .addComponentFillVertically(JPanel(), 0)
                .panel
        projectSettingsPanel.border =
            IdeBorderFactory.createTitledBorder(Strings.get("lt.settings.project_settings"))

        settingsPanel =
            FormBuilder.createFormBuilder()
                .addComponent(projectSettingsPanel)
                .addComponentFillVertically(JPanel(), 0)
                .panel
    }

    fun getPanel(): JPanel {
        return settingsPanel
    }
}
