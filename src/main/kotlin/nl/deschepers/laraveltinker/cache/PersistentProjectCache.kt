package nl.deschepers.laraveltinker.cache

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import nl.deschepers.laraveltinker.LaravelTinkerBundle

@State(
    reloadable = true,
    name = "laravelTinkerProject",
    storages = [Storage("laravel-tinker-plugin.xml")]
)
class PersistentProjectCache : PersistentStateComponent<PersistentProjectCache.State> {
    class State {
        var lastCode: String =
            LaravelTinkerBundle.message("lt.tinker.default.console") + "\n"
    }

    private var cacheState: State = State()

    override fun getState(): State {
        return cacheState
    }

    override fun loadState(state: State) {
        cacheState = state
    }
}
