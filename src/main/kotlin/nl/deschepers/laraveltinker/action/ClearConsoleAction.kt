package nl.deschepers.laraveltinker.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.toolwindow.TinkerOutputToolWindowFactory
import nl.deschepers.laraveltinker.util.getConsole

class ClearConsoleAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isVisible = true
        e.presentation.isEnabled = false
        e.presentation.text = Strings.get("lt.menu.action.clear_console")
        e.project ?: return
        val console = e.getConsole()
        e.presentation.isEnabled = console != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val console = e.getConsole()

        if (console != null) {
            TinkerOutputToolWindowFactory.tinkerOutputToolWindow[project]?.clearOutput()
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
