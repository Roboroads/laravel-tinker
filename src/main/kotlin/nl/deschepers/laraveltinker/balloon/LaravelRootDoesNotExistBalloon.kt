package nl.deschepers.laraveltinker.balloon

import com.intellij.notification.NotificationAction
import com.intellij.openapi.project.Project
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.action.EditLaravelTinkerProjectSettingsAction

class LaravelRootDoesNotExistBalloon(project: Project, triedPath: String) : Balloon(project) {
    override var isError: Boolean = true
    override var title: String? = Strings.get("lt.error.laravel_root_does_not_exist.title")
    override var content: String = Strings.get("lt.error.laravel_root_does_not_exist.message", triedPath)

    override fun getActions(): List<NotificationAction> {
        return listOf(EditLaravelTinkerProjectSettingsAction())
    }
}
