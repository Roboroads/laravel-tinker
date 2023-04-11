package nl.deschepers.laraveltinker.toolwindow

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.settings.GlobalSettingsState
import nl.deschepers.laraveltinker.util.HelperUtil
import org.apache.commons.lang.StringEscapeUtils.escapeHtml
import java.awt.Desktop
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.swing.JTextPane
import javax.swing.SizeRequirements
import javax.swing.event.HyperlinkEvent
import javax.swing.text.Element
import javax.swing.text.View
import javax.swing.text.ViewFactory
import javax.swing.text.html.HTMLEditorKit
import javax.swing.text.html.InlineView
import javax.swing.text.html.ParagraphView
import kotlin.concurrent.schedule
import kotlin.math.max

class TinkerOutputToolwindow(private val toolWindow: ToolWindow) : SimpleToolWindowPanel(
    true,
    true
) {
    private var tinkerOutput: JTextPane = JTextPane().apply {
        contentType = "text/html"
        background = null
        foreground = null
        disabledTextColor = null
        editorKit = HTMLEditorKit()
        isEditable = false
        addHyperlinkListener { e ->
            if (e.eventType == HyperlinkEvent.EventType.ACTIVATED &&
                Desktop.isDesktopSupported()
            ) {
                Desktop.getDesktop().browse(e.url.toURI())
            }
        }
    }
    private var outputText: String = ""
    private var outputTime: String = ""
    var plug: String? = null

    private var timer = false
    private val pluginSettings = GlobalSettingsState.getInstance()

    init {
        apply {
            add(JBScrollPane(tinkerOutput).apply {
                verticalScrollBar.unitIncrement = 1
                horizontalScrollBar.unitIncrement = 1
            })
        }
    }

    fun resetOutput() {
        toolWindow.title = "Output"
        outputTime =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        outputText = ""

        toolWindow.show()
        updateView()
    }

    fun addOutput(tinkerOutput: String) {
        outputText += tinkerOutput

        if (!timer) {
            timer = true

            Timer("UpdateTinkerOutput", false)
                .schedule(250) {
                    outputText = outputText
                        .replace("%%EOT%%", "")
                        .replace(Regex("Exit: {2}Goodbye$"), "")
                    updateView()
                    timer = false
                }
        }
    }

    private fun updateView() {
        val foregroundColor = HelperUtil.colorToHex(
            HighlighterColors.TEXT.defaultAttributes.foregroundColor ?: JBColor.BLACK
        )

        val globalScheme = EditorColorsManager.getInstance().globalScheme
        val timeString =
            if (pluginSettings.showExecutionStarted)
                Strings.get("lt.started_at", outputTime)
            else
                ""

        tinkerOutput.editorKit =
            if (pluginSettings.useWordWrapping) wordWrappingEditorKit() else HTMLEditorKit()

        this.tinkerOutput.text =
            """
            <html>
                <head>
                    <style>
                        body {
                           word-wrap: break-word;
                           color: $foregroundColor;
                           font-family: ${this.font.family};
                           font-size: ${globalScheme.editorFontSize}pt;
                        } 
                        pre, code {
                           font-family: '${globalScheme.editorFontName}';
                           font-size: ${globalScheme.editorFontSize}pt;
                        } 
                        .output {
                            padding: 5px; 
                            ${if (pluginSettings.useWordWrapping) "padding-left: 10px;" else ""}
                            ${if (pluginSettings.useWordWrapping) "text-indent: -5px;" else ""}
                        }
                        .header {
                            font-weight: bold;
                        }
                        a {
                           color: $foregroundColor;
                           text-decoration: underline;
                        }
                    </style>
                </head>
                <body>
                    <div class="header">
                        $timeString
                    </div>
                    <div class="output">
                        <pre><code>${ansiToHtml(outputText)}${if (plug != null) "\n\n" + plug else ""}</code></pre>
                    </div>
                </body>
            </html>
            """
    }

    private fun ansiToHtml(ansiText: String): String {
        if (ansiText.isEmpty()) {
            return ""
        }

        val keywordColor = HelperUtil.colorToHex(
            DefaultLanguageHighlighterColors.KEYWORD.defaultAttributes.foregroundColor
        )
        val intColor = HelperUtil.colorToHex(
            DefaultLanguageHighlighterColors.NUMBER.defaultAttributes.foregroundColor
        )
        val stringColor = HelperUtil.colorToHex(
            DefaultLanguageHighlighterColors.STRING.defaultAttributes.foregroundColor
        )
        val floatColor = HelperUtil.colorToHex(
            DefaultLanguageHighlighterColors.NUMBER.defaultAttributes.foregroundColor
        )
        val commentColor = HelperUtil.colorToHex(
            DefaultLanguageHighlighterColors.LINE_COMMENT.defaultAttributes.foregroundColor
        )

        val ansiRegex = Regex("\u001B\\[([0-9;]*)m")
        val ansiCommands = ansiRegex.findAll(ansiText).map { it.groupValues[1] }.toMutableList()

        if (ansiCommands.isEmpty()) {
            return escapeHtml(ansiText)
        }

        val textParts = ansiText.split(ansiRegex).toMutableList()
        var currentColor: String? = null
        var isBold = false
        var isUnderlined = false

        var htmlText = escapeHtml(textParts.removeFirst())

        println(ansiText)
        println(ansiText.replace(Regex("\u001B"), ""))

        while (ansiCommands.isNotEmpty()) {
            val command = ansiCommands.removeFirst()
            val text = textParts.removeFirst()

            when (command) {
                "0" -> {
                    currentColor = null
                    isBold = false
                    isUnderlined = false
                }

                "1" -> isBold = true
                "4" -> isUnderlined = true
                "22" -> isBold = false
                "24" -> isUnderlined = false
                "32" -> currentColor = stringColor
                "1;38;5;113" -> currentColor = stringColor
                "33" -> currentColor = floatColor
                "35" -> currentColor = intColor
                "1;38;5;38" -> currentColor = intColor
                "36" -> currentColor = keywordColor
                "38;5;38" -> currentColor = keywordColor
                "90" -> currentColor = commentColor
                "39" -> currentColor = null
                "0;38;5;208" -> currentColor = null
                else -> {
                    // Nothing we parse happened, no need to add a span
                    htmlText += escapeHtml(text)
                    continue
                }
            }

            var style = ""
            if (isBold) style += "font-weight: bold;"
            if (isUnderlined) style += "text-decoration: underline;"
            if (currentColor != null) style += "color: $currentColor;"

            htmlText += "</span><span style=\"$style\">"
            htmlText += escapeHtml(text)
        }

        // If residual text is in the buffer, which should not happen, add it to the output
        if (textParts.isNotEmpty()) {
            htmlText += escapeHtml(textParts.joinToString(""))
        }

        htmlText = ("<span>$htmlText</span>")
            // Remove unstyled spans
            .replace(Regex("(<span style=\"\">|<span>)(.*?)</span>", RegexOption.DOT_MATCHES_ALL), "$2")
            // Remove spans with only whitespace in it
            .replace(Regex("<span[^>]*?>([\\s\\n]*)</span>"), "$1")
            // Replace whisper with a gray span
            .replace("&lt;whisper&gt;", "<span style=\"color: gray;\">")
            .replace("&lt;/whisper&gt;", "</span>")

        return htmlText
    }

    private fun wordWrappingEditorKit() = object : HTMLEditorKit() {
        override fun getViewFactory(): ViewFactory {
            return object : HTMLFactory() {
                override fun create(e: Element): View {
                    val view = super.create(e)
                    if (view is InlineView) {
                        return object : InlineView(e) {
                            override fun getBreakWeight(
                                axis: Int,
                                pos: Float,
                                len: Float
                            ): Int {
                                return GoodBreakWeight
                            }

                            override fun breakView(
                                axis: Int,
                                p0: Int,
                                pos: Float,
                                len: Float
                            ): View {
                                if (axis == X_AXIS) {
                                    checkPainter()
                                    val p1 = glyphPainter.getBoundedPosition(this, p0, pos, len)
                                    return if (p0 == startOffset && p1 == endOffset) {
                                        this
                                    } else
                                        createFragment(p0, p1)
                                }
                                return this
                            }
                        }
                    } else if (view is ParagraphView) {
                        return object : ParagraphView(e) {
                            override fun calculateMinorAxisRequirements(
                                axis: Int,
                                r: SizeRequirements?
                            ): SizeRequirements {
                                var sizeRequirements: SizeRequirements? = r
                                if (sizeRequirements == null) {
                                    sizeRequirements = SizeRequirements()
                                }
                                val pref = layoutPool.getPreferredSpan(axis)
                                val min = layoutPool.getMinimumSpan(axis)
                                // Don't include insets, Box.getXXXSpan will include them.
                                sizeRequirements.minimum = min.toInt()
                                sizeRequirements.preferred =
                                    max(sizeRequirements.minimum, pref.toInt())
                                sizeRequirements.maximum = Int.MAX_VALUE
                                sizeRequirements.alignment = 0.5f
                                return sizeRequirements
                            }
                        }
                    }
                    return view
                }
            }
        }
    }
}
