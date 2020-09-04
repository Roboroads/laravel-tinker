package nl.deschepers.laraveltinker.toolwindow

import com.intellij.openapi.wm.ToolWindow
import nl.deschepers.laraveltinker.LaravelTinkerBundle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.JPanel
import javax.swing.JTextPane

class TinkerOutputToolwindow(private val toolWindow: ToolWindow?) {
    companion object {
        private const val COLOR1 = 0xFFFFFF
        private const val COLOR2 = 0x999999
    }

    private var tinkerOutputToolWindowContent: JPanel? = null
    private var tinkerOutput: JTextPane? = null
    private var outputText: String = ""
    private var outputTime: String = ""

    fun resetOutput() {
        tinkerOutput?.background = null
        tinkerOutput?.foreground = null
        tinkerOutput?.disabledTextColor = null
        tinkerOutput?.disabledTextColor = null

        outputTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        outputText = ""

        if (toolWindow != null) {
            toolWindow.show()
        }
        this.updateView()
    }

    fun addOutput(tinkerOutput: String) {
        outputText += tinkerOutput
        outputText = outputText.replace("%%EOT%%", "")
        this.updateView()
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
