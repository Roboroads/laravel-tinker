package nl.deschepers.laraveltinker.util

import com.intellij.ide.scratch.ScratchFileService
import com.intellij.ide.scratch.ScratchFileServiceImpl
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.FileIndex
import com.intellij.openapi.roots.impl.DirectoryIndex
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.jetbrains.php.lang.PhpLanguage
import nl.deschepers.laraveltinker.Strings

class TinkerConsoleUtil(val project: Project) {
    companion object {
        private val IS_TINKER_CONSOLE_KEY: Key<String> = Key.create("IsTinkerConsole")
    }

    fun getLastOpenTinkerConsole(): VirtualFile? {
        return getTinkerConsoleFiles()?.last()
    }

    fun getLastOpenOrCreateTinkerConsole(): VirtualFile {
        val lastOpen = getTinkerConsoleFiles()?.last()
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
            PhpArtisanTinkerUtil(
                project,
                document.text
            ).run()

            return true
        }

        return false
    }

    fun getTinkerConsoleFiles(): List<VirtualFile>? {
        val consolesPath = ScratchFileServiceImpl.getInstance().getRootPath(LaravelTinkerConsolesRootType.getInstance())
        val consolesDir = LocalFileSystem.getInstance().findFileByPath(consolesPath)

        return consolesDir?.children?.asList()
    }

    fun initializeExistingTinkerConsoles() {
        val files = getTinkerConsoleFiles()
        val fileService = ScratchFileService.getInstance()
        files?.forEach {
            fileService.scratchesMapping.setMapping(it, PhpLanguage.INSTANCE)
        }
    }

    private fun getTinkerConsole(option: ScratchFileService.Option, startingText: String = ""): VirtualFile? {
        getTinkerConsoleFiles()
        val tinkerConsole = LaravelTinkerConsolesRootType.getInstance().createScratchFile(
            project,
            Strings.get("lt.console.default_content") + startingText,
            option
        )

        tinkerConsole?.putUserData(IS_TINKER_CONSOLE_KEY, "true")

        return tinkerConsole
    }
}
