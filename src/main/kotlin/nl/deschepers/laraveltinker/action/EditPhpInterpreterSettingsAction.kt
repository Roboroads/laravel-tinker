package nl.deschepers.laraveltinker.action

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil

class EditPhpInterpreterSettingsAction : NotificationAction("PHP Interpreter Settings") {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        val project = e.project
        if (project != null) {
            ShowSettingsUtil.getInstance().showSettingsDialog(project, "Interpreter")
        }
    }
}