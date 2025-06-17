package nl.deschepers.laraveltinker.action

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.php.lang.PhpFileType
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.repository.ConsoleFileRepository

/** Always opens a new tinker console */
class OpenNewConsoleWithSelectionAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isVisible = false
        e.presentation.isEnabled = true
        e.presentation.text = Strings.get("lt.menu.action.open_new_console_with_selection")
        val currentEditor: Editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val virtualFile: VirtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        if (virtualFile.fileType is PhpFileType && currentEditor.selectionModel.hasSelection()) {
            e.presentation.isVisible = true
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val consoleFileRepository = ConsoleFileRepository(project)
        val currentEditor: Editor = e.getData(CommonDataKeys.EDITOR) ?: return

        if (currentEditor.selectionModel.hasSelection()) {
            val tinkerConsole =
                consoleFileRepository.createNewTinkerConsole(
                    "\n${currentEditor.selectionModel.selectedText!!}\n"
                )
            FileEditorManager.getInstance(project).openFile(tinkerConsole, true)
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
