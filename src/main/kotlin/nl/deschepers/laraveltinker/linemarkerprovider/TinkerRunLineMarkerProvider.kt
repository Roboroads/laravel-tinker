package nl.deschepers.laraveltinker.linemarkerprovider

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment.RIGHT
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.types.LaravelTinkerConsoleType
import nl.deschepers.laraveltinker.repository.ConsoleFileRepository
import java.awt.event.MouseEvent

class TinkerRunLineMarkerProvider : LineMarkerProvider {
    companion object {
        private const val PHP_OPEN_TAG = "php opening tag"
    }

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        val consoleFileRepository = ConsoleFileRepository(element.project)
        if (element.containingFile.virtualFile.fileType is LaravelTinkerConsoleType && element.elementType.toString() == PHP_OPEN_TAG) {
            return LineMarkerInfo(
                element,
                element.textRange,
                IconLoader.getIcon("icons/tinker-run.svg", javaClass),
                null,
                { _: MouseEvent, psiElement: PsiElement ->
                    consoleFileRepository.runFile(psiElement.containingFile.virtualFile)
                },
                RIGHT,
                { Strings.get("lt.run") }
            )
        }

        return null
    }
}
