package nl.deschepers.laraveltinker.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    reloadable = true,
    name = "laravelTinker",
    storages = [Storage("laravel-tinker-plugin.xml")]
)
class PersistentApplicationCache : PersistentStateComponent<PersistentApplicationCache.State> {
    companion object {
        val instance: PersistentApplicationCache
            get() = ServiceManager.getService(PersistentApplicationCache::class.java)
    }

    class State {
        var executionsCount: Int = 0
    }

    private var cacheState: State = State()

    override fun getState(): State {
        return cacheState
    }

    override fun loadState(state: State) {
        cacheState = state
    }
}
