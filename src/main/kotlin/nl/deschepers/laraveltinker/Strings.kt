package nl.deschepers.laraveltinker

import com.intellij.AbstractBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

@NonNls
private const val BUNDLE = "strings.strings"

object Strings : AbstractBundle(BUNDLE) {
    @JvmStatic
    fun get(
        @PropertyKey(resourceBundle = BUNDLE)
        key: String,
        vararg params: Any
    ) = getMessage(key, *params)
}
