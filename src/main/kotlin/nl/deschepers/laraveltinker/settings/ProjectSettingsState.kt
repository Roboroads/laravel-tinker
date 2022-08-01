package nl.deschepers.laraveltinker.settings

import com.google.gson.JsonObject
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import org.jetbrains.annotations.NotNull

@State(
    name = "org.intellij.sdk.settings.AppSettingsState",
    storages = [Storage("laravel-tinker.xml")]
)
class ProjectSettingsState : PersistentStateComponent<ProjectSettingsState> {
    var laravelRoot = ""
    var terminateApp = false

    companion object {
        fun getInstance(project: Project): ProjectSettingsState {
            return project.getService(ProjectSettingsState::class.java)
        }
    }

    override fun getState(): ProjectSettingsState {
        return this
    }

    override fun loadState(state: @NotNull ProjectSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    fun parseJson(): String {
        val settingsObject = JsonObject()
        settingsObject.addProperty("laravelRoot", this.laravelRoot)
        settingsObject.addProperty("terminateApp", this.terminateApp)
        return settingsObject.toString()
    }
}
