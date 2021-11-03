package nl.deschepers.laraveltinker.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent
import nl.deschepers.laraveltinker.Strings

class ProjectSettingsConfigurable(private var project: Project) : Configurable {
    private var projectSettingsComponent: ProjectSettingsComponent? = null

    override fun createComponent(): JComponent {
        projectSettingsComponent = ProjectSettingsComponent()
        return projectSettingsComponent!!.getPanel()
    }

    override fun isModified(): Boolean {
        val projectSettings = ProjectSettingsState.getInstance(project)
        return projectSettingsComponent!!.laravelRoot != projectSettings.laravelRoot
    }

    override fun apply() {
        val projectSettings = ProjectSettingsState.getInstance(project)
        projectSettings.laravelRoot = projectSettingsComponent!!.laravelRoot
    }

    override fun reset() {
        val projectSettings = ProjectSettingsState.getInstance(project)
        projectSettingsComponent!!.laravelRoot = projectSettings.laravelRoot
    }

    override fun getDisplayName(): String {
        return Strings.getMessage("name")
    }

    override fun disposeUIResources() {
        projectSettingsComponent = null
    }
}
