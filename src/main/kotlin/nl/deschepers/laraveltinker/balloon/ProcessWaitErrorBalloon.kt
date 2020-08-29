package nl.deschepers.laraveltinker.balloon

import com.intellij.notification.NotificationAction
import com.intellij.openapi.project.Project
import nl.deschepers.laraveltinker.LaravelTinkerBundle
import nl.deschepers.laraveltinker.action.EditPhpInterpreterSettingsAction

class ProcessWaitErrorBalloon(project: Project) : Balloon(project) {
    override var isError: Boolean = true
    override var title: String? = LaravelTinkerBundle.message("lt.error.php.interpreter.error")
    override var content: String =
        LaravelTinkerBundle.message("lt.error.stopped.waiting.for.process")
}