package nl.deschepers.laraveltinker.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import nl.deschepers.laraveltinker.editor.TinkerEditor

class StartLaravelTinkerAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)

        e.presentation.text = "Start new Tinker console"
        e.presentation.isEnabled = true
        e.presentation.isVisible = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getProject()
        if (project != null) {
            TinkerEditor(project)
        }
    }
}