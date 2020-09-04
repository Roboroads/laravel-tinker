package nl.deschepers.laraveltinker.editor

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.LightVirtualFile
import com.jetbrains.php.lang.PhpFileType
import nl.deschepers.laraveltinker.cache.PersistentProjectCache

class TinkerEditor(project: Project) {
    companion object {
        val openFiles = ArrayList<VirtualFile>()
    }

    var fileEditorManager: FileEditorManager = FileEditorManager.getInstance(project)

    init {
        val tinkerFile = LightVirtualFile(
            "Tinker Console",
            PhpFileType.INSTANCE,
            project.getService(PersistentProjectCache::class.java).state.lastCode
        )
        fileEditorManager.openFile(tinkerFile, true)
        openFiles.add(tinkerFile)
    }
}
