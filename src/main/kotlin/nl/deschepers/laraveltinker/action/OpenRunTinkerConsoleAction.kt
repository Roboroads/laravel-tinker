package nl.deschepers.laraveltinker.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFile
import nl.deschepers.laraveltinker.editor.TinkerConsole
import nl.deschepers.laraveltinker.run.PhpArtisanTinker

class OpenRunTinkerConsoleAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)

        e.presentation.isEnabled = true
        e.presentation.isVisible = true

        if (getTinkerConsoleFile(e) != null) {
            e.presentation.text = "Run Tinker Console"
        } else {
            e.presentation.text = "Start new Tinker console"
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val tinkerConsoleFile = getTinkerConsoleFile(e)
        if (tinkerConsoleFile != null) {
            val document = FileDocumentManager.getInstance().getDocument(tinkerConsoleFile)
            if (document !== null) {
                PhpArtisanTinker(
                    project,
                    document.text
                ).run()

                return
            }
        }

        TinkerConsole.open(project)
    }

    private fun getTinkerConsoleFile(e: AnActionEvent): VirtualFile? {
        val virtualFile: VirtualFile? = e.getData(CommonDataKeys.VIRTUAL_FILE)
        if (virtualFile != null && TinkerConsole.openFile == virtualFile) return virtualFile

        // Action was not run from an editor - see if one is open at all.
        val project = e.project
        if (project !== null) {
            for (editor in FileEditorManager.getInstance(project).selectedEditors) {
                if (TinkerConsole.openFile == editor.file) {
                    return editor.file
                }
            }
        }

        return null
    }
}
