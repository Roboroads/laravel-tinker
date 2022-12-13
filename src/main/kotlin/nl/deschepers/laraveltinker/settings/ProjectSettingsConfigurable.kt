package nl.deschepers.laraveltinker.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import nl.deschepers.laraveltinker.Strings
import javax.swing.JComponent

class ProjectSettingsConfigurable(private var project: Project) : Configurable {
    private var projectSettingsComponent: ProjectSettingsComponent? = null

    override fun createComponent(): JComponent {
        projectSettingsComponent = ProjectSettingsComponent()
        return projectSettingsComponent!!.getPanel()
    }

    override fun isModified(): Boolean {
        val projectSettings = ProjectSettingsState.getInstance(project)
        return projectSettingsComponent!!.laravelRoot != projectSettings.laravelRoot ||
            projectSettingsComponent!!.terminateApp != projectSettings.terminateApp ||
            projectSettingsComponent!!.vendorRoot != projectSettings.vendorRoot
    }

    override fun apply() {
        val projectSettings = ProjectSettingsState.getInstance(project)
        projectSettings.laravelRoot = projectSettingsComponent!!.laravelRoot
        projectSettings.terminateApp = projectSettingsComponent!!.terminateApp
        projectSettings.vendorRoot = projectSettingsComponent!!.vendorRoot
    }

    override fun reset() {
        val projectSettings = ProjectSettingsState.getInstance(project)
        projectSettingsComponent!!.laravelRoot = projectSettings.laravelRoot
        projectSettingsComponent!!.terminateApp = projectSettings.terminateApp
        projectSettingsComponent!!.vendorRoot = projectSettings.vendorRoot
    }

    override fun getDisplayName(): String {
        return Strings.getMessage("name")
    }

    override fun disposeUIResources() {
        projectSettingsComponent = null
    }
}
