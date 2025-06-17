package nl.deschepers.laraveltinker.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.repository.ConsoleFileRepository

/** Always opens a new tinker console */
class RunConsoleEditorContextAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isVisible = false
        e.presentation.isEnabled = false
        e.presentation.text = Strings.get("lt.menu.action.run_tinker_console")
        val project = e.project ?: return
        val consoleFileRepository = ConsoleFileRepository(project)

        val virtualFile: VirtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        if (consoleFileRepository.isConsole(virtualFile)) {
            e.presentation.isEnabled = true
            e.presentation.isVisible = true
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val consoleFileRepository = ConsoleFileRepository(project)
        val virtualFile: VirtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        if (consoleFileRepository.isConsole(virtualFile)) {
            consoleFileRepository.runFile(virtualFile)
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
