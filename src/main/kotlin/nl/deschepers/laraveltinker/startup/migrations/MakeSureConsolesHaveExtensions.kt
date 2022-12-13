package nl.deschepers.laraveltinker.startup.migrations

import com.intellij.ide.scratch.ScratchFileService
import nl.deschepers.laraveltinker.util.LaravelTinkerConsolesRootType
import java.io.File

class MakeSureConsolesHaveExtensions {

    fun up() {
        val consoleDirectory =
            ScratchFileService.getInstance()
                .getRootPath(LaravelTinkerConsolesRootType.getInstance())
        File(consoleDirectory).listFiles()
            ?.forEachIndexed { i, file ->
                file.renameTo(
                    File("${consoleDirectory}/LaravelTinkerConsole${if (i > 0) "_$i" else ""}.php")
                )
            }
    }
}
