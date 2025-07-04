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

/**
 *  The default 1.x tinker action: Opens the last console if none is open or Opens a new console
 * with your selected text or Switches to the tinker console tab is one is open or Runs the
 * currently viewing tinker tab.
 */
class SmartAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)

        e.presentation.isVisible = true
        e.presentation.isEnabled = false
        e.presentation.text =
            Strings.get("lt.menu.action.smart", Strings.get("lt.menu.action.open_new_console"))
        val project = e.project ?: return
        val consoleFileRepository = ConsoleFileRepository(project)

        e.presentation.isEnabled = true

        val virtualFile: VirtualFile? = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (virtualFile != null && consoleFileRepository.isConsole(virtualFile)) {
            e.presentation.text =
                Strings.get("lt.menu.action.smart", Strings.get("lt.menu.action.run_console"))
            return
        }

        for (editor in FileEditorManager.getInstance(project).selectedEditors) {
            if (editor.file != null && consoleFileRepository.isConsole(editor.file!!)) {
                e.presentation.text =
                    Strings.get(
                        "lt.menu.action.smart",
                        Strings.get("lt.menu.action.switch_to_tinker_console")
                    )
                return
            }
        }

        if (consoleFileRepository.getLastModified() != null) {
            e.presentation.text =
                Strings.get("lt.menu.action.smart", Strings.get("lt.menu.action.open_last_console"))
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val consoleFileRepository = ConsoleFileRepository(project)

        // First smart action - running with a console open: run
        val virtualFile: VirtualFile? = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (virtualFile != null && consoleFileRepository.isConsole(virtualFile) &&
            consoleFileRepository.runFile(virtualFile)
        )
            return

        // Second action: Open a new console with the selected text
        val currentEditor: Editor? = e.getData(CommonDataKeys.EDITOR)
        val isFile = currentEditor != null && virtualFile != null
        if (isFile && virtualFile.fileType is PhpFileType &&
            currentEditor.selectionModel.hasSelection()
        ) {
            val tinkerConsole =
                consoleFileRepository.createNewTinkerConsole(
                    "\n${currentEditor.selectionModel.selectedText!!}\n"
                )
            FileEditorManager.getInstance(project).openFile(tinkerConsole, true)
        }

        // Third action: Console is open already, just not active
        for (editor in FileEditorManager.getInstance(project).selectedEditors) {
            if (editor.file != null && consoleFileRepository.isConsole(editor.file!!)) {
                FileEditorManager.getInstance(project).openFile(editor.file!!, true)
                return
            }
        }

        // Else: (Re)open a tinker console
        val tinkerConsole = consoleFileRepository.getLastModifiedOrCreate()
        FileEditorManager.getInstance(project).openFile(tinkerConsole, true)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
