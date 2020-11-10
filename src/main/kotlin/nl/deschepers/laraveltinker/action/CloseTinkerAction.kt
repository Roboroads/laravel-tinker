package nl.deschepers.laraveltinker.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowManager
import nl.deschepers.laraveltinker.editor.TinkerConsole
import nl.deschepers.laraveltinker.run.PhpArtisanTinker
import nl.deschepers.laraveltinker.toolwindow.TinkerOutputToolWindowFactory

class CloseTinkerAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)

        e.presentation.isEnabled = true
        e.presentation.isVisible = false
        e.presentation.text = "Close Tinker console/output"
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        if(TinkerConsole.openFile != null) {
            FileEditorManager.getInstance(project).closeFile(TinkerConsole.openFile!!)
        }
        ToolWindowManager.getInstance(project).getToolWindow("Laravel Tinker")?.hide()
    }
}
