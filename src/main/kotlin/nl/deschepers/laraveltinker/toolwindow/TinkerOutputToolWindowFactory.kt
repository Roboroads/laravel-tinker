package nl.deschepers.laraveltinker.toolwindow

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
        val instance = TinkerOutputToolwindow(toolWindow)
        tinkerOutputToolWindow[project] = instance
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content: Content =
            contentFactory.createContent(instance.getContent(), "", false)
        toolWindow.contentManager.addContent(content)
    }
}
