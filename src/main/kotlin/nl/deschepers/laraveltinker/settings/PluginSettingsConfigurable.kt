package nl.deschepers.laraveltinker.settings

import com.intellij.openapi.options.Configurable
import nl.deschepers.laraveltinker.LaravelTinkerBundle
import javax.swing.JComponent

class PluginSettingsConfigurable : Configurable {
    private var pluginSettingsWindow: PluginSettingsWindow? = null

    override fun createComponent(): JComponent? {
        pluginSettingsWindow = PluginSettingsWindow()
        return pluginSettingsWindow!!.getPanel()
    }

    override fun isModified(): Boolean {
        val settings = PluginSettings.getInstance()
        return !pluginSettingsWindow!!.getCommandLineParams().equals(settings.commandLineParams)
    }

    override fun apply() {
        val settings = PluginSettings.getInstance()
        settings.commandLineParams = pluginSettingsWindow!!.getCommandLineParams()
    }

    override fun reset() {
        val settings = PluginSettings.getInstance()
        pluginSettingsWindow!!.setCommandLineParams(settings.commandLineParams)
    }

    override fun getDisplayName(): String {
        return LaravelTinkerBundle.getMessage("name")
    }

    override fun disposeUIResources() {
        pluginSettingsWindow = null
    }
}
