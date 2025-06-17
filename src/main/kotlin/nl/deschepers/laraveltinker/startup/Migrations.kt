package nl.deschepers.laraveltinker.startup

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import de.skuzzle.semantic.Version
import nl.deschepers.laraveltinker.settings.PersistentApplicationCache

class Migrations : ProjectActivity {
    override suspend fun execute(project: Project) {
        val currentMigrateVersion =
            Version.parseVersion(PersistentApplicationCache.instance.state.migrateVersion)
        val currentPluginVersion =
            Version.parseVersion(
                PluginManagerCore.getPlugin(PluginId.getId("nl.deschepers.laraveltinker"))!!.version
            )
        if (currentMigrateVersion == currentPluginVersion) return

//        if (currentMigrateVersion < Version.parseVersion("2.2.2-beta.3"))
//            MakeSureConsolesHaveExtensions().up()

        PersistentApplicationCache.instance.state.migrateVersion = currentPluginVersion.toString()
    }
}
