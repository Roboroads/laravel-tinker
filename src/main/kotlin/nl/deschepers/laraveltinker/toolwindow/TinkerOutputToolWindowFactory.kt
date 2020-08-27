package nl.deschepers.laraveltinker.toolwindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory




class TinkerOutputToolWindowFactory : ToolWindowFactory {
    companion object {
        var tinkerOutputToolWindow: TinkerOutputToolwindow? = null
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        tinkerOutputToolWindow = TinkerOutputToolwindow(toolWindow);
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content: Content = contentFactory.createContent(tinkerOutputToolWindow!!.getContent(), "", false)
        toolWindow.contentManager.addContent(content)
    }
}