package nl.deschepers.laraveltinker.balloon

import com.intellij.openapi.project.Project
import nl.deschepers.laraveltinker.Strings

class ProcessWaitErrorBalloon(project: Project) : Balloon(project) {
    override var isError: Boolean = true
    override var title: String? = Strings.get("lt.error.php.interpreter.error")
    override var content: String =
        Strings.get("lt.error.stopped.waiting.for.process")
}
