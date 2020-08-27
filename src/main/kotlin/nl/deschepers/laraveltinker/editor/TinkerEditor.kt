package nl.deschepers.laraveltinker.editor

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.LightVirtualFile
import com.jetbrains.php.lang.PhpFileType

class TinkerEditor(project: Project) {
    companion object {
        val openFiles = ArrayList<VirtualFile>()
    }

    var fileEditorManager: FileEditorManager = FileEditorManager.getInstance(project)

    init {
        val lvf = LightVirtualFile(
            "Tinker Console",
            PhpFileType.INSTANCE,
            "<?php\n// Tinker away!\necho 'PHP Artisan Tinker';"
        )
        fileEditorManager.openFile(lvf, true)
        openFiles.add(lvf)
    }
}
