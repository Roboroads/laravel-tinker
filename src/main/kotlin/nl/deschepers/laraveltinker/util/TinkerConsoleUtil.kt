package nl.deschepers.laraveltinker.util

import com.intellij.ide.scratch.ScratchFileService
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import nl.deschepers.laraveltinker.Strings

class TinkerConsoleUtil(val project: Project) {
    companion object {
        private val IS_TINKER_CONSOLE_KEY: Key<String> = Key.create("IsTinkerConsole")
    }

    fun getLastOpenTinkerConsole(): VirtualFile? {
        return ScratchFileService.getInstance().findFile(
            LaravelTinkerConsolesRootType.getInstance(),
            Strings.get("lt.console.name"),
            ScratchFileService.Option.existing_only
        )
    }

    fun getLastOpenOrCreateTinkerConsole(): VirtualFile {
        return getTinkerConsole(ScratchFileService.Option.create_if_missing)!!
    }

    fun createNewTinkerConsole(startingText: String = ""): VirtualFile {
        return getTinkerConsole(ScratchFileService.Option.create_new_always, startingText)!!
    }

    fun isTinkerConsole(file: VirtualFile): Boolean {
        val isTinkerConsole = file.getUserData(IS_TINKER_CONSOLE_KEY)
        return isTinkerConsole != null && isTinkerConsole == "true"
    }

    fun runTinkerOnFile(file: VirtualFile): Boolean {
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

    private fun getTinkerConsole(option: ScratchFileService.Option, startingText: String = ""): VirtualFile? {
        val tinkerConsole = LaravelTinkerConsolesRootType.getInstance().createScratchFile(
            project,
            Strings.get("lt.console.default_content") + startingText,
            option
        )

        tinkerConsole?.putUserData(IS_TINKER_CONSOLE_KEY, "true")

        return tinkerConsole
    }
}
