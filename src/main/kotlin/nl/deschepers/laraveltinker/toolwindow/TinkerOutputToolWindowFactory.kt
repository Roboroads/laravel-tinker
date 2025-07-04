package nl.deschepers.laraveltinker.toolwindow

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory

class TinkerOutputToolWindowFactory : ToolWindowFactory {
    companion object {
        var tinkerOutputToolWindow: MutableMap<Project, TinkerOutputToolwindow> = mutableMapOf()
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val actionManager = ActionManager.getInstance()
        toolWindow.setTitleActions(
            listOf(
                actionManager.getAction("nl.deschepers.laraveltinker.action.RunConsoleAction"),
                actionManager.getAction("nl.deschepers.laraveltinker.action.OpenNewConsoleAction"),
                actionManager.getAction("nl.deschepers.laraveltinker.action.OpenLastConsoleAction"),
                actionManager.getAction("nl.deschepers.laraveltinker.action.ClearConsoleAction")
            )
        )

        val instance = TinkerOutputToolwindow(toolWindow)
        tinkerOutputToolWindow[project] = instance
        val contentFactory = ApplicationManager.getApplication().getService(ContentFactory::class.java)
        val content: Content = contentFactory.createContent(instance, "", false)
        toolWindow.contentManager.addContent(content)
    }
}
