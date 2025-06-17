package nl.deschepers.laraveltinker.listener

import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.wm.ToolWindowManager
import nl.deschepers.laraveltinker.repository.ConsoleFileRepository
import nl.deschepers.laraveltinker.settings.GlobalSettingsState

class TinkerConsoleEditorManagerListener : FileEditorManagerListener {
    companion object {
        const val TOOL_WINDOW_ID = "Laravel Tinker"
    }

    override fun selectionChanged(event: FileEditorManagerEvent) {
        if(!GlobalSettingsState.getInstance().autoOpenCloseOutput) {
            return
        }

        val project = event.manager.project
        val consoleFileRepository = ConsoleFileRepository(project)
        val newFile = event.newFile

        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(TOOL_WINDOW_ID)
        if (newFile != null && consoleFileRepository.isConsole(newFile)) {
            toolWindow?.show()
        } else {
            toolWindow?.hide()
        }
    }
}
