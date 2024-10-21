package nl.deschepers.laraveltinker.util

import com.intellij.ide.scratch.ScratchFileServiceImpl
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

fun AnActionEvent.isEnabled(): Pair<Boolean, VirtualFile?> {

    val project = this.project!!
    var virtualFile: VirtualFile? = this.getData(PlatformDataKeys.VIRTUAL_FILE)
    var actionEnabled = false

    // Try opened files
    if (virtualFile == null) {
        for (file in FileEditorManager.getInstance(project).openFiles) {
            if (file != null && isTinkerConsole(file)) {
                virtualFile = file
                actionEnabled= true
                break
            }
        }
    } else {
        if (isTinkerConsole(virtualFile)) {
            actionEnabled = true
        }
    }

    return Pair(actionEnabled, virtualFile)
}

fun isTinkerConsole(file: VirtualFile): Boolean {
    return getTinkerConsoleFiles()?.contains(file) ?: false
}

fun getTinkerConsoleFiles(): List<VirtualFile>? {
    val consolesPath =
        ScratchFileServiceImpl.getInstance()
            .getRootPath(LaravelTinkerConsolesRootType.getInstance())
    val consolesDir = LocalFileSystem.getInstance().findFileByPath(consolesPath)

    return consolesDir?.children?.asList()
}
