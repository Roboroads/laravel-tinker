package nl.deschepers.laraveltinker.balloon

import com.intellij.notification.NotificationAction
import com.intellij.openapi.project.Project
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.action.EditPhpInterpreterSettingsAction

class NoPhpInterpreterBalloon(project: Project) : Balloon(project) {
    override var isError: Boolean = true
    override var title: String? = Strings.get("lt.error.no_php_interpreter")
    override var content: String = ""

    override fun getActions(): List<NotificationAction> {
        return listOf(EditPhpInterpreterSettingsAction())
    }
}
