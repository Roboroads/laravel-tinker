package nl.deschepers.laraveltinker.balloon

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project

class Balloon(private val project: Project) {
    var isError: Boolean = false
    var title: String? = null
    var content: String = ""

    fun show() {
        val notificationGroup =
            NotificationGroup("Laravel Tinker", NotificationDisplayType.BALLOON, true)
        val notification: Notification

        if (title != null) {
            notification = notificationGroup.createNotification(title!!, content,
                if (isError) NotificationType.ERROR else NotificationType.INFORMATION
            )
        } else {
            notification = notificationGroup.createNotification(content,
                if (isError) NotificationType.ERROR else NotificationType.INFORMATION
            )
        }

        this.getActions().forEach {
            notification.addAction(it)
        }

        Notifications.Bus.notify(notification, project)
    }

    fun getActions(): List<NotificationAction> {
        return emptyList()
    }
}
