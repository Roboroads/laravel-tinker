package nl.deschepers.laraveltinker.repository

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.settings.ProjectSettingsState
import nl.deschepers.laraveltinker.types.LaravelTinkerConsoleType
import nl.deschepers.laraveltinker.util.PhpArtisanTinkerUtil
import java.io.File

class ConsoleFileRepository(val project: Project) {
    fun getLastModified(): VirtualFile? {
        return getTinkerConsoleFiles().firstOrNull()
    }

    fun getLastModifiedOrCreate(initialContent: String? = null): VirtualFile {
        return getTinkerConsoleFiles().firstOrNull() ?: createNewTinkerConsole(initialContent)
    }

    fun createNewTinkerConsole(initialContent: String? = null): VirtualFile {
        val projectSettings = ProjectSettingsState.getInstance(project)
        val consoleDirectory = File(project.basePath ?: "", projectSettings.tinkerConsoleRoot)
        if (!consoleDirectory.exists()) {
            consoleDirectory.mkdirs()
            File(consoleDirectory, ".gitignore").writeText("*")
        }

        var followNumber = 0
        while (true) {
            val suffix = if (followNumber == 0) "" else "_$followNumber"
            val newConsole = File(consoleDirectory, "console${suffix}.tinker.php")
            if (!newConsole.exists()) {
                newConsole.createNewFile()
                newConsole.writeText("<?php\n${initialContent ?: Strings.getMessage("lt.console.default_content")}\n")
                return VfsUtil.findFileByIoFile(newConsole, true)!!
            }
            followNumber++
        }
    }

    fun isConsole(file: VirtualFile?): Boolean {
        return file?.fileType is LaravelTinkerConsoleType
    }

    fun runFile(file: VirtualFile): Boolean {
        val document = FileDocumentManager.getInstance().getDocument(file)
        if (document !== null) {
            PhpArtisanTinkerUtil(project, document.text).run()

            return true
        }

        return false
    }

    fun getTinkerConsoleFiles(): List<VirtualFile> {
        val files = FileTypeIndex.getFiles(LaravelTinkerConsoleType.INSTANCE, GlobalSearchScope.projectScope(project))
            .sortedByDescending { it.modificationStamp }
        return files
    }
}
