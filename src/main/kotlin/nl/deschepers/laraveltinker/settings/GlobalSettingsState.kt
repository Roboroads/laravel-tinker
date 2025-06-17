package nl.deschepers.laraveltinker.settings

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "nl.deschepers.laraveltinker.settings.AppSettingsState",
    storages = [Storage("laravel-tinker-plugin-settings.xml")]
)
class GlobalSettingsState : PersistentStateComponent<GlobalSettingsState>, Disposable {
    var showExecutionStarted = true
    var showExecutionEnded = true
    var useWordWrapping = true
    var autoOpenCloseOutput = true
    var patreonKey = ""

    companion object {
        fun getInstance(): GlobalSettingsState {
            return ApplicationManager.getApplication().getService(GlobalSettingsState::class.java)
        }
    }

    override fun getState(): GlobalSettingsState {
        return this
    }

    override fun loadState(state: GlobalSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    override fun dispose() {
        //
    }
}
