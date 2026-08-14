package com.hm.viewdemo.aibot

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.hm.viewdemo.R

/** Renders Markdown paragraphs and pipe tables inside one assistant bubble. */
internal class AiMarkdownView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
    }

    fun setMarkdown(markdown: String) {
        removeAllViews()
        val lines = markdown.replace("\r\n", "\n").split('\n')
        val paragraph = mutableListOf<String>()
        var index = 0

        fun flushParagraph() {
            if (paragraph.any { it.isNotBlank() }) {
                addParagraph(paragraph.joinToString("\n"))
            }
            paragraph.clear()
        }

        while (index < lines.size) {
            val table = parseTable(lines, index)
            if (table != null) {
                flushParagraph()
                addTable(table.first)
                index = table.second
            } else {
                paragraph += lines[index]
                index++
            }
        }
        flushParagraph()
    }

    private fun addParagraph(markdown: String) {
        val textView = TextView(context).apply {
            setTextColor(ContextCompat.getColor(context, R.color.ai_bot_text_primary))
            textSize = 15f
            setLineSpacing(dp(3).toFloat(), 1f)
            linksClickable = true
            movementMethod = LinkMovementMethod.getInstance()
            setLinkTextColor(ContextCompat.getColor(context, R.color.ai_bot_primary))
            highlightColor = Color.TRANSPARENT
            setText(MarkdownRenderer.render(markdown), TextView.BufferType.SPANNABLE)
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        addView(textView)
    }

    private fun addTable(tableData: TableData) {
        val horizontalScroll = HorizontalScrollView(context).apply {
            isHorizontalScrollBarEnabled = true
            overScrollMode = OVER_SCROLL_IF_CONTENT_SCROLLS
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dp(6)
                bottomMargin = dp(8)
            }
        }
        val table = TableLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        addTableRow(table, tableData.headers, isHeader = true)
        tableData.rows.forEach { addTableRow(table, it, isHeader = false) }
        horizontalScroll.addView(table)
        addView(horizontalScroll)
    }

    private fun addTableRow(table: TableLayout, values: List<String>, isHeader: Boolean) {
        val row = TableRow(context).apply {
            gravity = Gravity.CENTER_VERTICAL
            layoutParams = TableLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        values.forEach { value ->
            val cell = TextView(context).apply {
                setTextColor(ContextCompat.getColor(context, R.color.ai_bot_text_primary))
                textSize = 13f
                minWidth = dp(88)
                gravity = Gravity.CENTER_VERTICAL
                typeface = if (isHeader) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
                setText(MarkdownRenderer.render(value), TextView.BufferType.SPANNABLE)
                setLinkTextColor(ContextCompat.getColor(context, R.color.ai_bot_primary))
                movementMethod = LinkMovementMethod.getInstance()
                background = ContextCompat.getDrawable(
                    context,
                    if (isHeader) R.drawable.bg_ai_bot_table_header else R.drawable.bg_ai_bot_table_cell
                )
            }
            row.addView(cell, TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ))
        }
        table.addView(row)
    }

    private fun parseTable(lines: List<String>, start: Int): Pair<TableData, Int>? {
        if (start + 1 >= lines.size) return null
        val headers = splitCells(lines[start])
        val separators = splitCells(lines[start + 1])
        if (headers.size < 2 || separators.size != headers.size ||
            separators.any { !it.matches(Regex(":?-{3,}:?")) }
        ) {
            return null
        }

        val rows = mutableListOf<List<String>>()
        var next = start + 2
        while (next < lines.size && lines[next].isNotBlank() && lines[next].contains('|')) {
            val cells = splitCells(lines[next])
            if (cells.isNotEmpty()) rows += cells
            next++
        }
        return TableData(headers, rows) to next
    }

    private fun splitCells(line: String): List<String> {
        val normalized = line.trim().removePrefix("|").removeSuffix("|")
        return normalized.split('|').map { it.trim() }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private data class TableData(
        val headers: List<String>,
        val rows: List<List<String>>
    )
}
