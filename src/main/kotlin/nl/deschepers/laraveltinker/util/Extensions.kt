package nl.deschepers.laraveltinker.util

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFile

fun AnActionEvent.isEnabled(): Pair<Boolean, VirtualFile?> {

    val project = this.project!!
    val tinkerConsoleUtil = TinkerConsoleUtil(project)
    var virtualFile: VirtualFile? = this.getData(CommonDataKeys.VIRTUAL_FILE)
    var actionEnabled = false

    // Try opened files
    if (virtualFile == null) {
        for (file in FileEditorManager.getInstance(project).openFiles) {
            if (file != null && tinkerConsoleUtil.isTinkerConsole(file)) {
                virtualFile = file
                actionEnabled= true
                break
            }
        }
    } else {
        if (tinkerConsoleUtil.isTinkerConsole(virtualFile)) {
            actionEnabled = true
        }
    }

    return Pair(actionEnabled, virtualFile)
}
