package nl.deschepers.laraveltinker.balloon

import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.project.Project

open class Balloon(private val project: Project) {
    open var isError: Boolean = false
    open var title: String? = null
    open var content: String = ""

    fun show() {
        val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("Laravel Tinker")
        val notification: Notification

        if (title != null) {
            notification = notificationGroup.createNotification(
                title!!,
                content,
                if (isError) NotificationType.ERROR else NotificationType.INFORMATION
            )
        } else {
            notification = notificationGroup.createNotification(
                content,
                if (isError) NotificationType.ERROR else NotificationType.INFORMATION
            )
        }

        this.getActions().forEach {
            notification.addAction(it)
        }

        Notifications.Bus.notify(notification, project)
    }

    open fun getActions(): List<NotificationAction> {
        return emptyList()
    }
}
