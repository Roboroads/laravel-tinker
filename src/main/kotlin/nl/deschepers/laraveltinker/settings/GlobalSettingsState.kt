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
class GlobalSettingsState : PersistentStateComponent<GlobalSettingsState> {
    var showExecutionStarted = true
    var showExecutionEnded = true
    var useWordWrapping = true
    var patreonKey = ""

    companion object {
        fun getInstance(): GlobalSettingsState {
            return ServiceManager.getService(GlobalSettingsState::class.java)
        }
    }

    override fun getState(): GlobalSettingsState {
        return this
    }

    override fun loadState(state: @NotNull GlobalSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }
}
