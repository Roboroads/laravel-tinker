package nl.deschepers.laraveltinker.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.repository.ConsoleFileRepository

/** Always opens a new tinker console */
class OpenNewConsoleAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isVisible = true
        e.presentation.isEnabled = false
        e.presentation.text = Strings.get("lt.menu.action.open_new_console")
        if (e.project == null) return
        e.presentation.isEnabled = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val consoleFileRepository = ConsoleFileRepository(project)
        val tinkerConsole = consoleFileRepository.createNewTinkerConsole()
        FileEditorManager.getInstance(project).openFile(tinkerConsole, true)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
