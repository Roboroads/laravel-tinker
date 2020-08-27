package nl.deschepers.laraveltinker.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.vfs.VirtualFile
import nl.deschepers.laraveltinker.editor.TinkerEditor
import nl.deschepers.laraveltinker.run.PhpArtisanTinker

class OpenRunTinkerConsoleAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)

        e.presentation.isEnabled = true
        e.presentation.isVisible = true

        if (getTinkerEditor(e) != null) {
            e.presentation.text = "Run Tinker Console"
        } else {
            e.presentation.text = "Start new Tinker console"
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val tinkerEditor = getTinkerEditor(e)
        if (tinkerEditor != null) {
            PhpArtisanTinker(project, tinkerEditor.document.text).run()
        } else {
            TinkerEditor(project)
        }
    }

    private fun getTinkerEditor(e: AnActionEvent): Editor? {
        val runnableVirtualFile: VirtualFile? = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val currentEditor: Editor? = e.getData(CommonDataKeys.EDITOR)
        return if (runnableVirtualFile != null &&
            TinkerEditor.openFiles.contains(runnableVirtualFile) &&
            currentEditor != null
        ) currentEditor
        else null
    }
}
