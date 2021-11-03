package nl.deschepers.laraveltinker.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(reloadable = true, name = "laravelTinker", storages = [Storage("laravel-tinker-plugin.xml")])
class PersistentApplicationCache : PersistentStateComponent<PersistentApplicationCache.State> {
    companion object {
        val instance: PersistentApplicationCache
            get() =
                ApplicationManager.getApplication()
                    .getService(PersistentApplicationCache::class.java)
    }

    class State {
        var executionsCount = 0
        var migrateVersion = "0.0.1"
    }

    private var cacheState: State = State()

    override fun getState(): State {
        return cacheState
    }

    override fun loadState(state: State) {
        cacheState = state
    }
}
