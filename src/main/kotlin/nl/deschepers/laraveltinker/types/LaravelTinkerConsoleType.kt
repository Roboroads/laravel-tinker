package nl.deschepers.laraveltinker.types

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import com.jetbrains.php.lang.PhpLanguage
import nl.deschepers.laraveltinker.Strings
import org.jetbrains.annotations.Nls
import javax.swing.Icon


class LaravelTinkerConsoleType :
    LanguageFileType(PhpLanguage.INSTANCE) {

    companion object {
        val INSTANCE: LaravelTinkerConsoleType = LaravelTinkerConsoleType()
    }

    override fun getName(): String {
        return Strings.get("lt.console.name")
    }

    override fun getDescription(): String {
        return Strings.get("lt.console.name")
    }

    override fun getDefaultExtension(): String {
        return "tinker.php"
    }

    override fun getIcon(): Icon {
        return IconLoader.getIcon("icons/icon.svg", javaClass)
    }

    override fun getDisplayName(): @Nls String {
        return Strings.get("lt.console.name")
    }
}
