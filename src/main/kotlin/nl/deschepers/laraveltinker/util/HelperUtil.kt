package nl.deschepers.laraveltinker.util

import java.awt.Color

object HelperUtil {
    fun colorToHex(color: Color): String {
        return "#" + Integer.toHexString(color.rgb).substring(2)
    }
}
