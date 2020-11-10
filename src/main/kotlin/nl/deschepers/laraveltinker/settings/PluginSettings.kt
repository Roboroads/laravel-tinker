package nl.deschepers.laraveltinker.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jetbrains.annotations.NotNull

@State(
    name = "org.intellij.sdk.settings.AppSettingsState",
    storages = [Storage("laravel-tinker-plugin-settings.xml")]
)
class PluginSettings : PersistentStateComponent<PluginSettings> {
    var commandLineParams: String = ""

    companion object {
        fun getInstance(): PluginSettings {
            return ServiceManager.getService(PluginSettings::class.java)
        }
    }

    override fun getState(): PluginSettings? {
        return this
    }

    override fun loadState(state: @NotNull PluginSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
