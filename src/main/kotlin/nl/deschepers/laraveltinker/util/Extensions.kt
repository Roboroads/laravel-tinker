package nl.deschepers.laraveltinker.util

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFile
import nl.deschepers.laraveltinker.repository.ConsoleFileRepository

fun AnActionEvent.getConsole(): VirtualFile? {
    val project = this.project!!
    val virtualFile: VirtualFile? = this.getData(PlatformDataKeys.VIRTUAL_FILE)

    val consoleFileRepository = ConsoleFileRepository(project)

    if (consoleFileRepository.isConsole(virtualFile)) {
        return virtualFile
    }

    for (file in FileEditorManager.getInstance(project).openFiles) {
        if (consoleFileRepository.isConsole(file)) {
            return file
        }
    }

    return null
}
