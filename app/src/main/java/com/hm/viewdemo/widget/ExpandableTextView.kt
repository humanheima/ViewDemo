package com.hm.viewdemo.widget

import android.content.Context
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.DynamicDrawableSpan.ALIGN_CENTER
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.hm.viewdemo.R
import com.hm.viewdemo.dp2px

/**
 * Created by p_dmweidu on 2025/6/5
 * Desc: 结尾是详情的+图标的TextView，待完善。
 */
class ExpandableTextView : AppCompatTextView {

    var isTruncated: Boolean = false
        private set

    /**
     * 空白字符
     */
    private val space = " "

    /**
     * 一个空格的宽度
     */
    private var spaceWidth = 0f

    private var endResId = R.drawable.ic_text_right_arrow

    companion object {
        private const val TAG = "ExpandableTextView"
        private const val MAX_LINES = 3

    }


    private val MORE_TEXT = "详情"

    /**
     * 14dp，图标的宽度
     */
    private var dp14 = 0

    private var widthOfChinese = 0

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        maxLines = MAX_LINES
        spaceWidth = paint.measureText(space)
        dp14 = 14.dp2px(context)
        //一个“中”的宽度，比图标宽度要宽
        widthOfChinese = paint.measureText("中").toInt()
        Log.d(TAG, "init: dp14 = $dp14 spaceWidth = $spaceWidth widthOfChinese = $widthOfChinese")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val layout = layout
        Log.d(TAG, "onMeasure: layout = $layout")
        if (layout != null) {
            val lineCount = layout.lineCount
            if (lineCount > MAX_LINES) {
                isTruncated = true
                truncateText()
            } else {
                isTruncated = false
                text = text // 恢复原始文本
            }
        }
    }

    private fun truncateText() {
        val layout = layout ?: return

        // 获取第三行的起始字符索引
        val thirdLineStart = layout.getLineStart(MAX_LINES - 1)
        val thirdLineEnd = layout.getLineEnd(MAX_LINES - 1)

        // 获取原始文本
        val originalText = text.toString()
        if (thirdLineEnd >= originalText.length) return

        // 计算第三行能显示的最大字符数（留出“详情”空间）
        val thirdLineText = originalText.substring(thirdLineStart, thirdLineEnd)
        var maxLength =
            thirdLineText.length - MORE_TEXT.length - 2// 预留“详情”和省略号和向右箭头的宽度，省略号一个宽度，向右箭头一个宽度

        if (maxLength <= 0) {
            maxLength = 0
        }

        // 截断文本
        val substring = originalText.substring(0, thirdLineStart + maxLength)
        var truncatedText = "$substring...$MORE_TEXT"

        val availableWidth = width - paddingLeft - paddingRight
        var insertSpaceCount = 0

        //第三行的文字
        val thirdLineTruncatedText =
            originalText.substring(thirdLineStart, thirdLineStart + maxLength) + "..." + MORE_TEXT

        var measureTextWidth = paint.measureText(thirdLineTruncatedText)

        Log.d(
            TAG,
            "truncateText: thirdLineTruncatedText = $thirdLineTruncatedText measureTextWidth = $measureTextWidth availableWidth = $availableWidth"
        )

        measureTextWidth += dp14//加上图标的宽度

        while (measureTextWidth < availableWidth) {
            Log.d(TAG, "truncateText: measureTextWidth = $measureTextWidth")
            insertSpaceCount++
            measureTextWidth += spaceWidth
        }
        Log.d(
            TAG,
            "truncateText: 跳出循环 measureTextWidth = $measureTextWidth insertSpaceCount = $insertSpaceCount availableWidth = $availableWidth"
        )
        //大于两个空白字符的宽度才调整
        if (insertSpaceCount > 2) {
            val stringBuilder = StringBuilder(substring)
            stringBuilder.append("...")
            for (i in 0 until insertSpaceCount - 2) {
                stringBuilder.append(space)
            }
            stringBuilder.append(MORE_TEXT)
            truncatedText = stringBuilder.toString()
        }

        // 创建 SpannableString ，最后一个空格展示向右的箭头
        truncatedText += " "
        val spannable = SpannableString(truncatedText)
        spannable.setSpan(
            ForegroundColorSpan(-0xffff01),  // 蓝色
            truncatedText.length - 1 - MORE_TEXT.length,
            truncatedText.length - 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val drawable = ContextCompat.getDrawable(context, endResId)

        var imageSpan: ImageSpan? = null
        drawable?.let {
            it.setBounds(0, 0, dp14, dp14)
            // 创建 ImageSpan
            imageSpan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ImageSpan(drawable, ALIGN_CENTER)
            } else {
                ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
            }
        }

        spannable.setSpan(
            imageSpan,
            truncatedText.length - 1,
            truncatedText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        text = spannable
        movementMethod = LinkMovementMethod.getInstance()
    }

}