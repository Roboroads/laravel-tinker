package nl.deschepers.laraveltinker.linemarkerprovider

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons.RunConfigurations.TestState.Run
import com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment.RIGHT
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.util.TinkerConsoleUtil
import java.awt.event.MouseEvent

class TinkerRunLineMarkerProvider : LineMarkerProvider {
    companion object {
        private const val PHP_OPEN_TAG = "php opening tag"
    }

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        val tinkerConsoleUtil = TinkerConsoleUtil(element.project)
        if (
            tinkerConsoleUtil.isTinkerConsole(element.containingFile.virtualFile) &&
            element.elementType.toString() == PHP_OPEN_TAG
        ) return LineMarkerInfo(
            element,
            element.textRange,
            Run,
            null,
            { _: MouseEvent, psiElement: PsiElement ->
                tinkerConsoleUtil.runTinkerWithFile(psiElement.containingFile.virtualFile)
            },
            RIGHT,
            { Strings.get("lt.run") }
        )

        return null
    }
}
