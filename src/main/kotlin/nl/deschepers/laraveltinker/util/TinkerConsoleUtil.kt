package nl.deschepers.laraveltinker.util

import com.intellij.ide.scratch.ScratchFileService
import com.intellij.ide.scratch.ScratchFileServiceImpl
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.php.lang.PhpLanguage
import nl.deschepers.laraveltinker.Strings

class TinkerConsoleUtil(val project: Project) {
    fun getLastOpenTinkerConsole(): VirtualFile? {
        return getTinkerConsoleFiles()?.lastOrNull()
    }

    fun getLastOpenOrCreateTinkerConsole(): VirtualFile {
        val lastOpen = getTinkerConsoleFiles()?.lastOrNull()
        return lastOpen ?: getTinkerConsole(ScratchFileService.Option.create_if_missing)!!
    }

    fun createNewTinkerConsole(startingText: String = ""): VirtualFile {
        return getTinkerConsole(ScratchFileService.Option.create_new_always, startingText)!!
    }

    fun isTinkerConsole(file: VirtualFile): Boolean {
        return getTinkerConsoleFiles()?.contains(file) ?: false
    }

    fun runTinkerWithFile(file: VirtualFile): Boolean {
        val document = FileDocumentManager.getInstance().getDocument(file)
        if (document !== null) {
            PhpArtisanTinkerUtil(project, document.text).run()

            return true
        }

        return false
    }

    private fun getTinkerConsoleFiles(): List<VirtualFile>? {
        val consolesPath =
            ScratchFileServiceImpl.getInstance()
                .getRootPath(LaravelTinkerConsolesRootType.getInstance())
        val consolesDir = LocalFileSystem.getInstance().findFileByPath(consolesPath)

        return consolesDir?.children?.asList()
    }

    fun initializeExistingTinkerConsoles() {
        val files = getTinkerConsoleFiles()
        val fileService = ScratchFileService.getInstance()
        files?.forEach { fileService.scratchesMapping.setMapping(it, PhpLanguage.INSTANCE) }
    }

    private fun getTinkerConsole(
        option: ScratchFileService.Option,
        startingText: String = ""
    ): VirtualFile? {
        getTinkerConsoleFiles()

        return LaravelTinkerConsolesRootType.getInstance()
            .createScratchFile(
                project,
                Strings.get("lt.console.default_content") + startingText,
                option
            )
    }
}
