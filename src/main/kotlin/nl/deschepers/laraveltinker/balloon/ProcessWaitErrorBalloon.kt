package nl.deschepers.laraveltinker.balloon

import com.intellij.openapi.project.Project
import nl.deschepers.laraveltinker.LaravelTinkerBundle

class ProcessWaitErrorBalloon(project: Project) : Balloon(project) {
    override var isError: Boolean = true
    override var title: String? = LaravelTinkerBundle.message("lt.error.php.interpreter.error")
    override var content: String =
        LaravelTinkerBundle.message("lt.error.stopped.waiting.for.process")
}
