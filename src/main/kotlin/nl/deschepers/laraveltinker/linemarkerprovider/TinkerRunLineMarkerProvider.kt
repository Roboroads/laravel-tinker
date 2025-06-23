package nl.deschepers.laraveltinker.linemarkerprovider

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment.RIGHT
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import com.jetbrains.php.lang.psi.stubs.PhpFileElementType
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.types.LaravelTinkerConsoleType
import nl.deschepers.laraveltinker.repository.ConsoleFileRepository
import java.awt.event.MouseEvent

class TinkerRunLineMarkerProvider : LineMarkerProvider {
    companion object {
        private const val PHP_OPEN_TAG = "php opening tag"
    }

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        val isTinkerConsole = element.containingFile.virtualFile.fileType is LaravelTinkerConsoleType
        val isOpenTag = element.elementType.toString() == PHP_OPEN_TAG
        val isEndOfFile = element.textRange.endOffset == element.containingFile.textRange.endOffset && element.elementType is PhpFileElementType

        if (isTinkerConsole && (isOpenTag || isEndOfFile)) {
            return LineMarkerInfo(
                element,
                TextRange(element.textRange.endOffset, element.textRange.endOffset),
                IconLoader.getIcon("icons/tinker-run.svg", javaClass),
                null,
                { _: MouseEvent, psiElement: PsiElement ->
                    ConsoleFileRepository(element.project).runFile(psiElement.containingFile.virtualFile)
                },
                RIGHT,
                { Strings.get("lt.run") }
            )
        }

        return null
    }
}
