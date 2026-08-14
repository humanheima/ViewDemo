package com.hm.viewdemo.aibot

import android.text.Spanned
import androidx.core.text.HtmlCompat

/**
 * A deliberately small Markdown renderer for chat responses.
 * It keeps the demo dependency-free while covering the syntax normally used
 * in an AI response: headings, emphasis, lists, links, inline code and fences.
 */
object MarkdownRenderer {

    fun render(markdown: String): Spanned {
        val lines = markdown.replace("\r\n", "\n").split('\n')
        val html = StringBuilder()
        var inCodeBlock = false

        lines.forEachIndexed { index, rawLine ->
            val line = rawLine.trimEnd()
            if (line.trimStart().startsWith("```")) {
                if (inCodeBlock) {
                    html.append("</tt></pre>")
                } else {
                    html.append("<pre><tt><font color=\"#4D5870\">")
                }
                inCodeBlock = !inCodeBlock
            } else if (inCodeBlock) {
                html.append(escape(line).replace(" ", "&nbsp;"))
                if (index < lines.lastIndex) html.append("<br>")
            } else {
                html.append(formatLine(line))
                if (index < lines.lastIndex) html.append("<br>")
            }
        }

        if (inCodeBlock) html.append("</tt></pre>")
        return HtmlCompat.fromHtml(html.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun formatLine(line: String): String {
        if (line.isBlank()) return "<br>"

        val escaped = escape(line)
        return when {
            escaped.startsWith("###### ") -> "<big><b>${inline(escaped.substring(7))}</b></big>"
            escaped.startsWith("##### ") -> "<big><b>${inline(escaped.substring(6))}</b></big>"
            escaped.startsWith("#### ") -> "<big><b>${inline(escaped.substring(5))}</b></big>"
            escaped.startsWith("### ") -> "<big><b>${inline(escaped.substring(4))}</b></big>"
            escaped.startsWith("## ") -> "<big><b>${inline(escaped.substring(3))}</b></big>"
            escaped.startsWith("# ") -> "<big><b>${inline(escaped.substring(2))}</b></big>"
            escaped.startsWith("- ") -> "&#8226;&nbsp;${inline(escaped.substring(2))}"
            escaped.startsWith("* ") -> "&#8226;&nbsp;${inline(escaped.substring(2))}"
            escaped.matches(Regex("\\d+\\. .*")) -> {
                val markerEnd = escaped.indexOf('.') + 1
                "<b>${escaped.substring(0, markerEnd)}</b>${inline(escaped.substring(markerEnd))}"
            }
            line.startsWith("> ") -> "<font color=\"#6B7280\">${inline(escaped)}</font>"
            else -> inline(escaped)
        }
    }

    private fun inline(escaped: String): String {
        var result = escaped
        result = result.replace(Regex("\\[([^]]+)]\\(([^)]+)\\)")) {
            "<a href=\"${it.groupValues[2]}\">${it.groupValues[1]}</a>"
        }
        result = result.replace(Regex("`([^`]+)`"), "<tt><font color=\"#4D5870\">$1</font></tt>")
        result = result.replace(Regex("\\*\\*([^*]+)\\*\\*"), "<b>$1</b>")
        result = result.replace(Regex("__([^_]+)__"), "<b>$1</b>")
        result = result.replace(Regex("(?<!\\*)\\*([^*]+)\\*(?!\\*)"), "<i>$1</i>")
        result = result.replace(Regex("(?<!_)_([^_]+)_(?!_)"), "<i>$1</i>")
        return result
    }

    private fun escape(value: String): String = value
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
}
