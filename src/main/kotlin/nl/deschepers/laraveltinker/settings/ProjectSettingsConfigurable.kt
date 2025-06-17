package nl.deschepers.laraveltinker.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import nl.deschepers.laraveltinker.Strings
import javax.swing.JComponent

class ProjectSettingsConfigurable(private var project: Project) : Configurable {
    private var projectSettingsComponent: ProjectSettingsComponent = ProjectSettingsComponent()

    override fun createComponent(): JComponent {
        return projectSettingsComponent.getPanel()
    }

    override fun isModified(): Boolean {
        val projectSettings = ProjectSettingsState.getInstance(project)
        val basePath = project.basePath ?: ""

        return projectSettingsComponent.laravelRoot != projectSettings.laravelRoot ||
            projectSettingsComponent.terminateApp != projectSettings.terminateApp ||
            projectSettingsComponent.vendorRoot != projectSettings.vendorRoot ||
            projectSettingsComponent.tinkerConsoleRoot.removePrefix(basePath).removePrefix("/") != projectSettings.tinkerConsoleRoot
    }

    override fun apply() {
        val projectSettings = ProjectSettingsState.getInstance(project)
        val basePath = project.basePath ?: "*** nonexistingpath"

        projectSettings.laravelRoot = projectSettingsComponent.laravelRoot
        projectSettings.terminateApp = projectSettingsComponent.terminateApp
        projectSettings.vendorRoot = projectSettingsComponent.vendorRoot


        if (projectSettingsComponent.tinkerConsoleRoot.startsWith(basePath)) {
            projectSettings.tinkerConsoleRoot = projectSettingsComponent.tinkerConsoleRoot.removePrefix(basePath).removePrefix("/")
        } else {
            throw ConfigurationException(Strings.getMessage("lt.settings.error.console_root_invalid.message") , Strings.getMessage("lt.settings.error.console_root_invalid.title"))
        }
    }

    override fun reset() {
        val projectSettings = ProjectSettingsState.getInstance(project)
        val basePath = project.basePath ?: ""

        projectSettingsComponent.laravelRoot = projectSettings.laravelRoot
        projectSettingsComponent.terminateApp = projectSettings.terminateApp
        projectSettingsComponent.vendorRoot = projectSettings.vendorRoot
        projectSettingsComponent.tinkerConsoleRoot = "${basePath}/${projectSettings.tinkerConsoleRoot}"
    }

    override fun getDisplayName(): String {
        return Strings.getMessage("name")
    }
}
