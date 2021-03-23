package nl.deschepers.laraveltinker.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import nl.deschepers.laraveltinker.util.TinkerConsoleUtil

class ProjectOpenListener : ProjectManagerListener {
    override fun projectOpened(project: Project) {
        super.projectOpened(project)
        TinkerConsoleUtil(project).initializeExistingTinkerConsoles()
    }
}
