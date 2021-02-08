package nl.deschepers.laraveltinker.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.vfs.VirtualFile
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.util.TinkerConsoleUtil

/** Always opens a new tinker console */
class RunConsoleAction : AnAction() {
    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isVisible = true
        e.presentation.isEnabled = false
        e.presentation.text = Strings.get("lt.menu.action.run_console")
        val project = e.project ?: return
        val tinkerConsoleUtil = TinkerConsoleUtil(project)

        val virtualFile: VirtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        if (tinkerConsoleUtil.isTinkerConsole(virtualFile)) {
            e.presentation.isEnabled = true
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val tinkerConsoleUtil = TinkerConsoleUtil(project)
        val virtualFile: VirtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return

        if (tinkerConsoleUtil.isTinkerConsole(virtualFile)) {
            tinkerConsoleUtil.runTinkerOnFile(virtualFile)
        }
    }
}
