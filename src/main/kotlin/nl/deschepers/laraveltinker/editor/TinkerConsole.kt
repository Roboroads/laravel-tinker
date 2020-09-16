package nl.deschepers.laraveltinker.editor

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.testFramework.LightVirtualFile
import com.jetbrains.php.lang.PhpFileType
import nl.deschepers.laraveltinker.cache.PersistentProjectCache

object TinkerConsole {
    var openFile: VirtualFile? = null

    fun open(project: Project) {
        if (openFile == null) {
            openFile = LightVirtualFile(
                "Tinker Console",
                PhpFileType.INSTANCE,
                project.getService(PersistentProjectCache::class.java).state.lastCode
            )
        }

        FileEditorManager.getInstance(project).openFile(openFile!!, true)
    }
}
