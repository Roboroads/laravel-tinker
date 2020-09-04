package nl.deschepers.laraveltinker.toolwindow

import com.intellij.openapi.wm.ToolWindow
import nl.deschepers.laraveltinker.LaravelTinkerBundle
import java.awt.Desktop
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.event.HyperlinkEvent
import kotlin.concurrent.schedule

class TinkerOutputToolwindow(private val toolWindow: ToolWindow?) {
    private var tinkerOutputToolWindowContent: JPanel? = null
    private var tinkerOutput: JTextPane? = null
    private var outputText: String = ""
    private var outputTime: String = ""

    private var timer = false;

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

        if(!timer) {
            timer = true;

            Timer("UpdateTinkerOutput", false).schedule(250) {
                outputText = outputText.replace("%%EOT%%", "")
                updateView()
                timer = false;
            }
        }
    }

    fun updateView() {
        this.tinkerOutput!!.text = "<html>" +
            "<head>" +
            "<style>" +
            "body{word-wrap:break-word;} " +
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
            outputText +
            "</code></pre>" +
            "</div>" +
            "</body>" +
            "</html>"
    }

    fun getContent(): JPanel? {
        return tinkerOutputToolWindowContent
    }
}
