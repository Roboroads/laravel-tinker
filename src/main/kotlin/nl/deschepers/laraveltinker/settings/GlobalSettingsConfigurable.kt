package nl.deschepers.laraveltinker.settings

import com.intellij.openapi.options.Configurable
import nl.deschepers.laraveltinker.Strings
import javax.swing.JComponent

class GlobalSettingsConfigurable : Configurable {
    private var appSettingsComponent: GlobalSettingsComponent? = null

    override fun createComponent(): JComponent {
        appSettingsComponent = GlobalSettingsComponent()
        return appSettingsComponent!!.getPanel()
    }

    override fun isModified(): Boolean {
        val appSettings = GlobalSettingsState.getInstance()
        return appSettingsComponent!!.showExecutionStarted != appSettings.showExecutionStarted ||
            appSettingsComponent!!.showExecutionEnded != appSettings.showExecutionEnded ||
            appSettingsComponent!!.useWordWrapping != appSettings.useWordWrapping ||
            appSettingsComponent!!.patreonKey != appSettings.patreonKey
    }

    override fun apply() {
        val appSettings = GlobalSettingsState.getInstance()
        appSettings.showExecutionStarted = appSettingsComponent!!.showExecutionStarted
        appSettings.showExecutionEnded = appSettingsComponent!!.showExecutionEnded
        appSettings.useWordWrapping = appSettingsComponent!!.useWordWrapping
        appSettings.patreonKey = appSettingsComponent!!.patreonKey
    }

    override fun reset() {
        val appSettings = GlobalSettingsState.getInstance()
        appSettingsComponent!!.showExecutionStarted = appSettings.showExecutionStarted
        appSettingsComponent!!.showExecutionEnded = appSettings.showExecutionEnded
        appSettingsComponent!!.useWordWrapping = appSettings.useWordWrapping
        appSettingsComponent!!.patreonKey = appSettings.patreonKey
    }

    override fun getDisplayName(): String {
        return Strings.getMessage("name")
    }

    override fun disposeUIResources() {
        appSettingsComponent = null
    }
}
