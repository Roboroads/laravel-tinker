package nl.deschepers.laraveltinker.linemarkerprovider

import com.intellij.codeHighlighting.Pass.UPDATE_ALL
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.icons.AllIcons.RunConfigurations.TestState.Run
import com.intellij.openapi.editor.markup.GutterIconRenderer.Alignment.RIGHT
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import nl.deschepers.laraveltinker.editor.TinkerEditor
import nl.deschepers.laraveltinker.run.PhpArtisanTinker
import java.awt.event.MouseEvent

class TinkerRunLineMarkerProvider : LineMarkerProvider {
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        if (TinkerEditor.openFiles.contains(element.containingFile.virtualFile) &&
            element.elementType.toString() == "php opening tag"
        ) {
            return LineMarkerInfo(
                element,
                element.textRange,
                Run,
                UPDATE_ALL,
                null,
                { _: MouseEvent, psiElement: PsiElement ->
                    PhpArtisanTinker(
                        psiElement.project,
                        psiElement.containingFile.originalFile.text
                    ).run()
                },
                RIGHT
            )
        }
        return null
    }
}
