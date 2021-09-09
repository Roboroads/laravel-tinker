package nl.deschepers.laraveltinker.settings

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
}
