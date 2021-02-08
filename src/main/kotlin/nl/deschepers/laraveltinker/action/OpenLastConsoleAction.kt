package nl.deschepers.laraveltinker.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.util.TinkerConsoleUtil

/** Always opens a new tinker console */
class OpenLastConsoleAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isVisible = true
        e.presentation.isEnabled = false
        e.presentation.text = Strings.get("lt.menu.action.open_last_console")
        val project = e.project ?: return
        val tinkerConsoleUtil = TinkerConsoleUtil(project)

        if (tinkerConsoleUtil.getLastOpenTinkerConsole() != null) {
            e.presentation.isEnabled = true
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val tinkerConsoleUtil = TinkerConsoleUtil(project)

        val tinkerConsole = tinkerConsoleUtil.getLastOpenTinkerConsole()
        if (tinkerConsole != null) {
            FileEditorManager.getInstance(project).openFile(tinkerConsole, true)
        }
    }
}
