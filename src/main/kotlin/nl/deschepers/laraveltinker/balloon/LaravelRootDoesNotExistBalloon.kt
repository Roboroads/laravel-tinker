package nl.deschepers.laraveltinker.balloon

import com.intellij.notification.NotificationAction
import com.intellij.openapi.project.Project
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.action.EditLaravelTinkerProjectSettingsAction

class LaravelRootDoesNotExistBalloon(project: Project) : Balloon(project) {
    override var isError: Boolean = false
    override var title: String? = Strings.get("lt.error.laravel_root.does_not_exist.title")
    override var content: String =
        Strings.get("lt.error.laravel_root.does_not_exist.message")

    override fun getActions(): List<NotificationAction> {
        return listOf(EditLaravelTinkerProjectSettingsAction())
    }
}
