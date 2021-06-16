package nl.deschepers.laraveltinker.action

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.settings.ProjectSettingsConfigurable

class EditLaravelTinkerProjectSettingsAction : NotificationAction(
    Strings.get("lt.settings.tinker_project_settings")
) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        val project = e.project
        if (project != null) {
            ShowSettingsUtil.getInstance().showSettingsDialog(
                project,
                ProjectSettingsConfigurable::class.java
            )
        }
    }
}
