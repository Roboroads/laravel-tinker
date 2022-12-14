package nl.deschepers.laraveltinker.toolwindow

import com.intellij.application.options.EditorFontsConstants
import com.intellij.ide.ui.UISettings
import com.intellij.openapi.editor.EditorSettings
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.options.FontSize
import com.intellij.openapi.wm.ToolWindow
import com.intellij.util.FontUtil
import nl.deschepers.laraveltinker.Strings
import nl.deschepers.laraveltinker.settings.GlobalSettingsState
import nl.deschepers.laraveltinker.util.HelperUtil
import java.awt.Color
import java.awt.Desktop
import java.awt.Dimension
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.swing.JPanel
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

class TinkerOutputToolwindow(private val toolWindow: ToolWindow) {
    private var tinkerOutputToolWindowContent: JPanel? = null
    private var tinkerOutput: JTextPane? = null
    private var outputText: String = ""
    private var outputTime: String = ""
    var plug: String? = null

    private var timer = false
    private val pluginSettings = GlobalSettingsState.getInstance()

    init {
        tinkerOutput!!
            .addHyperlinkListener { e ->
                if (e.eventType == HyperlinkEvent.EventType.ACTIVATED &&
                    Desktop.isDesktopSupported()
                ) {
                    Desktop.getDesktop().browse(e.url.toURI())
                }
            }

        tinkerOutput!!.background = null
        tinkerOutput!!.foreground = null
        tinkerOutput!!.disabledTextColor = null
    }

    fun resetOutput() {
        toolWindow.title = "Output"
        outputTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
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
        val color = HelperUtil.colorToHex(HighlighterColors.TEXT.defaultAttributes.foregroundColor ?: Color.BLACK)
        val globalScheme = EditorColorsManager.getInstance().globalScheme
        val timeString =
            if (pluginSettings.showExecutionStarted)
                Strings.get("lt.started_at", outputTime)
            else
                ""

        if (pluginSettings.useWordWrapping) {
            setupWordWrapping()
        } else {
            tinkerOutput!!.editorKit = HTMLEditorKit()
        }

        this.tinkerOutput!!.text =
            """
            <html>
                <head>
                    <style>
                        body {
                           word-wrap: break-word;
                           color: $color;
                           font-family: ${tinkerOutputToolWindowContent!!.font.family};
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
                           color: $color;
                           text-decoration: underline;
                        }
                    </style>
                </head>
                <body>
                    <div class="header">
                        $timeString
                    </div>
                    <div class="output">
                        <pre><code>$outputText${if (plug != null) "\n\n" + plug else ""}</code></pre>
                    </div>
                </body>
            </html>
            """
    }

    fun getContent(): JPanel? {
        return tinkerOutputToolWindowContent
    }

    private fun setupWordWrapping() {
        tinkerOutput!!.editorKit = object : HTMLEditorKit() {
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
        tinkerOutput!!.contentType = "text/html"
        tinkerOutput!!.minimumSize = Dimension(0, 0)
    }
}
