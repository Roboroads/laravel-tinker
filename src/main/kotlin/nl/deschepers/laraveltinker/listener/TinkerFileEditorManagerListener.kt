package nl.deschepers.laraveltinker.listener

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindowManager
import nl.deschepers.laraveltinker.util.isTinkerConsole

class TinkerFileEditorManagerListener : FileEditorManagerListener {

    companion object {
        const val TOOL_WINDOW_ID = "Laravel Tinker"
    }

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        super.fileOpened(source, file)

        if ( isTinkerConsole(file)) {
            ToolWindowManager.getInstance(source.project).getToolWindow(TOOL_WINDOW_ID)?.show()
        }

    }

    override fun fileClosed(source: FileEditorManager, file: VirtualFile) {
        super.fileClosed(source, file)
        if ( isTinkerConsole(file)) {
            ToolWindowManager.getInstance(source.project).getToolWindow(TOOL_WINDOW_ID)?.hide()
        }
    }
}
