package nl.deschepers.laraveltinker.action

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.jetbrains.php.config.PhpProjectConfigurable
import nl.deschepers.laraveltinker.Strings

class EditPhpInterpreterSettingsAction : NotificationAction(
    Strings.get("lt.php.interpreter.settings")
) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        val project = e.project
        if (project != null) {
            ShowSettingsUtil.getInstance().showSettingsDialog(
                project,
                PhpProjectConfigurable::class.java
            )
        }
    }
}
