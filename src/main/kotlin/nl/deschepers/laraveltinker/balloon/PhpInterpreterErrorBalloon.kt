package nl.deschepers.laraveltinker.balloon

import com.intellij.notification.NotificationAction
import com.intellij.openapi.project.Project
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.action.EditPhpInterpreterSettingsAction

class PhpInterpreterErrorBalloon(project: Project, errorText: String) : Balloon(project) {
    override var isError: Boolean = true
    override var title: String? = Strings.get("lt.error.php_interpreter_error")
    override var content: String =
        Strings.get("lt.error.please_check_interpreter_settings") + "\n" + errorText

    override fun getActions(): List<NotificationAction> {
        return listOf(EditPhpInterpreterSettingsAction())
    }
}
