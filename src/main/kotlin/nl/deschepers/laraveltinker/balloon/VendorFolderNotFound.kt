package nl.deschepers.laraveltinker.balloon

import com.intellij.notification.NotificationAction
import com.intellij.openapi.project.Project
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.action.EditLaravelTinkerProjectSettingsAction

class VendorFolderNotFound(project: Project, vendorPath: String) : Balloon(project) {
    override var isError: Boolean = true
    override var title: String? = Strings.get("lt.error.laravel_root_no_vendor.title")
    override var content: String = Strings.get("lt.error.laravel_root_no_vendor.message", vendorPath)

    override fun getActions(): List<NotificationAction> {
        return listOf(EditLaravelTinkerProjectSettingsAction())
    }
}
