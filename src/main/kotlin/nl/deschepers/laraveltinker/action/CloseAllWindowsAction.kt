package nl.deschepers.laraveltinker.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.wm.ToolWindowManager
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.util.TinkerConsoleUtil

class CloseAllWindowsAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)

        e.presentation.isEnabled = true
        e.presentation.isVisible = true
        e.presentation.text = Strings.get("lt.menu.action.close_all_tinker_windows")
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val tinkerConsoleUtil = TinkerConsoleUtil(project)

        for (file in FileEditorManager.getInstance(project).openFiles) {
            if (file != null && tinkerConsoleUtil.isTinkerConsole(file)) {
                FileEditorManager.getInstance(project).closeFile(file)
            }
        }

        ToolWindowManager.getInstance(project).getToolWindow("Laravel Tinker")?.hide()
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
