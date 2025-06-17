package nl.deschepers.laraveltinker.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.repository.ConsoleFileRepository

/** Always opens a new tinker console */
class SwitchToConsoleTabAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isVisible = true
        e.presentation.isEnabled = false
        e.presentation.text = Strings.get("lt.menu.action.switch_to_tinker_console")
        val project = e.project ?: return
        val consoleFileRepository = ConsoleFileRepository(project)

        for (file in FileEditorManager.getInstance(project).openFiles) {
            if (file != null && consoleFileRepository.isConsole(file)) {
                e.presentation.isEnabled = true
                return
            }
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val consoleFileRepository = ConsoleFileRepository(project)

        for (file in FileEditorManager.getInstance(project).openFiles) {
            if (file != null && consoleFileRepository.isConsole(file)) {
                FileEditorManager.getInstance(project).openFile(file, true)
                return
            }
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
