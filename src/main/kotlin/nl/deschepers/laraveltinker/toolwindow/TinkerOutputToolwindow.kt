package nl.deschepers.laraveltinker.toolwindow

import com.intellij.openapi.wm.ToolWindow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.JEditorPane
import javax.swing.JPanel

class TinkerOutputToolwindow(private val toolWindow: ToolWindow) {
    private var tinkerOutputToolWindowContent: JPanel? = null
    private var tinkerOutput: JEditorPane? = null

    fun setTinkerOutput(tinkerOutput: List<String>) {
        this.tinkerOutput!!.text = "<html>" +
            "<head>" +
            "<style>.output{padding:5px;} .header{font-weight:bold;}</style>" +
            "</head>" +
            "<body>" +
            "<div class=\"header\">" +
            "// Laravel Tinker Output at " +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
            "</div>" +
            "<div class=\"output\">" +
            tinkerOutput.joinToString("") +
            "</div>" +
            "</body>" +
            "</html>"

        System.out.println(this.tinkerOutput!!.text)

        toolWindow.show()
    }

    fun getContent(): JPanel? {
        return tinkerOutputToolWindowContent
    }
}
