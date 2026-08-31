package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.text.style.QuoteSpan
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R

/**
 * Demonstrates paragraph-level BulletSpan and QuoteSpan usage.
 */
class BulletQuoteSpanActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, BulletQuoteSpanActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bullet_quote_span)

        findViewById<android.widget.TextView>(R.id.tv_bullet_content).text = buildBulletText()
        findViewById<android.widget.TextView>(R.id.tv_quote_content).text = buildQuoteText()
    }

    private fun buildBulletText(): CharSequence {
        val content = "第一项：BulletSpan 为每个段落绘制一个圆点。\n" +
            "第二项：同一段落中的多行文字会保持统一的缩进。\n" +
            "第三项：项目符号不需要把圆点写进实际文本。"
        val result = SpannableStringBuilder(content)

        // Paragraph spans should include the newline separating adjacent paragraphs.
        var paragraphStart = 0
        content.split("\n").forEachIndexed { index, paragraph ->
            val paragraphEnd = paragraphStart + paragraph.length + if (index < 2) 1 else 0
            result.setSpan(
                BulletSpan(24),
                paragraphStart,
                paragraphEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            paragraphStart = paragraphEnd
        }
        return result
    }

    private fun buildQuoteText(): CharSequence {
        val quote = "好的排版不仅是改变颜色和字号，\n还要让内容的层次一眼可见。\nQuoteSpan 会在段落左侧绘制引用线。"
        return SpannableStringBuilder(quote).apply {
            setSpan(
                QuoteSpan(Color.rgb(63, 81, 181)),
                0,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}
