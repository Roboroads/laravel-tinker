package nl.deschepers.laraveltinker.startup

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import de.skuzzle.semantic.Version
import nl.deschepers.laraveltinker.settings.PersistentApplicationCache
import nl.deschepers.laraveltinker.startup.migrations.MakeSureConsolesHaveExtensions

class Migrations : StartupActivity {
    override fun runActivity(project: Project) {
        val currentMigrateVersion = Version.parseVersion(PersistentApplicationCache.instance.state.migrateVersion)
        val currentPluginVersion = Version.parseVersion(PluginManagerCore.getPlugin(PluginId.getId("nl.deschepers.laraveltinker"))!!.version)
        if (currentMigrateVersion == currentPluginVersion) return

        if (currentMigrateVersion < Version.parseVersion("2.2.2-rc.2")) MakeSureConsolesHaveExtensions().up()

        PersistentApplicationCache.instance.state.migrateVersion = currentPluginVersion.toString()
    }
}
