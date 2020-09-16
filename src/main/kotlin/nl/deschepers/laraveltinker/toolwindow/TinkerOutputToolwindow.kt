package nl.deschepers.laraveltinker.toolwindow

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.wm.ToolWindow
import nl.deschepers.laraveltinker.LaravelTinkerBundle
import java.awt.Color
import java.awt.Desktop
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Timer
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.event.HyperlinkEvent
import kotlin.concurrent.schedule

class TinkerOutputToolwindow(private val toolWindow: ToolWindow?) {
    private var tinkerOutputToolWindowContent: JPanel? = null
    private var tinkerOutput: JTextPane? = null
    private var titlePane: JTextPane? = null
    private var outputText: String = ""
    private var outputTime: String = ""

    private var timer = false

    init {
        tinkerOutput!!.addHyperlinkListener { e ->
            if (
                e.eventType == HyperlinkEvent.EventType.ACTIVATED &&
                Desktop.isDesktopSupported()
            ) {
                Desktop.getDesktop().browse(e.url.toURI())
            }
        }

        tinkerOutput!!.background = null
        tinkerOutput!!.foreground = null
        tinkerOutput!!.disabledTextColor = null
        tinkerOutput!!.disabledTextColor = null
    }

    fun resetOutput() {

        outputTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        outputText = ""

        if (toolWindow != null) {
            toolWindow.show()
        }
        updateView()
    }

    fun addOutput(tinkerOutput: String) {
        outputText += tinkerOutput

        if (!timer) {
            timer = true

            Timer("UpdateTinkerOutput", false).schedule(250) {
                outputText = outputText.replace("%%EOT%%", "")
                updateView()
                timer = false
            }
        }
    }

    fun toHex(color: Color): String {
        return "#" + Integer.toHexString(color.rgb).substring(2)
    }

    fun updateView() {
        this.tinkerOutput!!.text = "<html>" +
            "<head>" +
            "<style>" +
            "body{word-wrap:break-word; color:${toHex(titlePane!!.foreground)}; font-family:${titlePane!!.font.family}; " +
            "background-color:#;} " +
            ".output{padding:5px;} " +
            ".header{font-weight:bold;}" +
            "</style>" +
            "</head>" +
            "<body>" +
            "<div class=\"header\">" +
            LaravelTinkerBundle.message("lt.started.at", outputTime) +
            "</div>" +
            "<div class=\"output\">" +
            "<pre><code>" +
            highlightSyntax("\n" + outputText) +
            "</code></pre>" +
            "</div>" +
            "</body>" +
            "</html>"
    }

    fun getContent(): JPanel? {
        return tinkerOutputToolWindowContent
    }

    private fun highlightSyntax(text: String): String {
        val stringColor = DefaultLanguageHighlighterColors.STRING.defaultAttributes.foregroundColor
        val numberColor = DefaultLanguageHighlighterColors.NUMBER.defaultAttributes.foregroundColor
        val propColor = DefaultLanguageHighlighterColors.INSTANCE_FIELD.defaultAttributes
            .foregroundColor

        val regex = Regex("(.*\n=>)(.*)", RegexOption.DOT_MATCHES_ALL);
        return text.replace(regex, "$1") +
            text.replace(regex, "$2")
                .replace( //Strings in array before =>
                    Regex("\"((?:[^\"\\\\]|\\\\.)*)\"\\s=>"),
                    "&quot;<font color=\"${toHex(propColor)}\">$1</font>&quot;" +
                        " =>"
                )
                .replace( //Ints in array before =>
                    Regex("([0-9]+)\\s=>"),
                    "<font color=\"${toHex(propColor)}\">$1</font> =>"
                )
                .replace( //String in objects before :
                    Regex("\\+\"((?:[^\"\\\\]|\\\\.)*)\":"),
                    "+&quot;<b>$1</b>&quot;:"
                )
                .replace( //Strings as values
                    Regex("(=>|:)\\s\"((?:[^\"\\\\]|\\\\.)*)\""),
                    "$1 &quot;<font color=\"${toHex(stringColor)}\">$2</font>&quot;"
                )
                .replace( //Ints as values
                    Regex("(=>|:)\\s([0-9]+)"),
                    "$1 <font color=\"${toHex(numberColor)}\">$2</font>"
                )
    }
}
