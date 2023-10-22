package com.hm.viewdemo.widget

import android.content.Context
import android.text.Layout
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2023/10/18
 * Desc: TextView + 查看详情，查看详情固定在末尾，不会紧跟...
 *
 *1. 要获取最后一行文字的宽度
 * 现在还有问题，一行占不满。
 */
class SeeDetailTextStyle1 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    private val TAG: String = "SeeDetailTextStyle1"

    private lateinit var tvTitle: TextView
    private lateinit var tvSeeDetail: TextView

    /**
     * 详情 的宽度，写死一个宽度
     */
    private var suffixWidth: Int = 0

    /**
     * 省略号的宽度
     */
    private var widthOfEllipsis: Int = 48

    /**
     * 一行金句语音可以占用的宽度
     */
    private var oneLineWidth: Int = 0

    /**
     * 两行金句语音可以占用的宽度
     */
    private var twoLineWidth: Int = 0

    /**
     * 三行金句语音可以占用的宽度
     */
    private var threeLineWidth: Int = 0

    /**
     * 3行金句语音可以占用的宽度减去省略号的宽度
     */
    private var threeLineWidthSubtractWidthOfEllipsis: Int = 0

    /**
     * 金句语音原文控件的宽度，也写死
     */
    private var contentWidth: Int = 0

    /**
     * 一个汉字的的宽度，换行的时候，会有影响
     */
    private var oneChineseWidth: Int = 0

    private var oneChinese: String = "\n"

    private var threeDot: String = "..."

    private var contentText: String = ""

    //最多3行，下标是2
    private var maxLines = 2

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.layout_see_detail_textview, this)
        tvTitle = findViewById(R.id.tv_child_tab_title)
        tvSeeDetail = findViewById(R.id.tv_see_detail)

//        widthOfEllipsis = tvTitle?.paint?.measureText("...")?.toInt() ?: 0
//        oneChineseWidth = tvTitle?.paint?.measureText(oneChinese)?.toInt() ?: 0
//
//        contentWidth = ScreenUtil.getScreenWidth(context)
//        //suffixWidth = tvSeeDetail?.paint?.measureText(tvSeeDetail?.text.toString())?.toInt() ?: 0
//        //Note: 这里使用控件的宽度
//        suffixWidth = ScreenUtil.dpToPx(context, 48)
//        Log.i(
//            TAG,
//            "initView: suffixWidth = $suffixWidth tvVoiceContentWidth = $contentWidth"
//        )
//        oneLineWidth = contentWidth - suffixWidth
//        Log.i(TAG, "initView: oneLineWidth = $oneLineWidth")
//        twoLineWidth = (contentWidth * 2) - suffixWidth - oneChineseWidth
//        Log.i(TAG, "initView: twoLineWidth = $twoLineWidth")
//        threeLineWidth = (contentWidth * 3) - suffixWidth - 2 * oneChineseWidth
//        Log.i(TAG, "initView: threeLineWidth = $threeLineWidth")
//
//
//        threeLineWidthSubtractWidthOfEllipsis = threeLineWidth - widthOfEllipsis
    }

    fun setText(titleText: String?) {
        contentText = titleText ?: ""
        val contentLineCount = tvTitle.lineCount
        Log.i(TAG, "setText: contentLineCount = $contentLineCount")
        //下标从0开始
        clipText(maxLines - 1)
//        if (titleText.isNullOrEmpty() || true) {
//            tvTitle?.text = ""
//            return
//        }
//        val text: String = titleText
//        val measureTextWidth = tvTitle?.paint?.measureText(text)?.toInt() ?: 0
//
//        if (measureTextWidth <= oneLineWidth) {
//            tvTitle?.text = text
//            return
//        }
//
//        //顶到详情的位置了，换行
//        if (measureTextWidth <= contentWidth) {
//            tvTitle?.text = "$text\n"
//            return
//        }
//
//        //加一个换行的高度
//        val measureTextWidthAddOneReturnWidth = measureTextWidth + oneChineseWidth
//        if (measureTextWidthAddOneReturnWidth <= twoLineWidth) {
//            tvTitle?.text = text
//            return
//        }
//        if (measureTextWidthAddOneReturnWidth <= (contentWidth * 2)) {
//            tvTitle?.text = "$text\n"
//            return
//        }
//        //加2个换行的高度
//        val measureTextWidthAddTwoReturnWidth = measureTextWidth + oneChineseWidth + oneChineseWidth
//
//        if (measureTextWidthAddTwoReturnWidth <= threeLineWidth) {
//            tvTitle?.text = text
//            return
//        }
//        // 三行
//        tvTitle?.text = getVoiceTextWithSuffix(text)
    }

    /**
     * 根据内容宽度减去切换按钮宽度，裁掉一些文字换成"..."
     *
     * @param lastLineIndex 最后一行的序号
     */
    private fun clipText(lastLineIndex: Int) {
        tvTitle.post {
            tvTitle.text = contentText
            var contentLineCount = tvTitle.lineCount - 1
            Log.i(TAG, "clipText: contentLineCount = $contentLineCount")
            if (contentLineCount > maxLines) {
                contentLineCount = maxLines
            }
            // 容器给予的可用宽度
            val containerAvailableWidth = this.measuredWidth - this.paddingLeft - this.paddingRight
            // 真实可用宽度 = 容器给予的可用宽度 - 切换按钮的宽度 - "..."的宽度
            val realAvailableWidth: Int =
                containerAvailableWidth - tvSeeDetail.measuredWidth - Layout.getDesiredWidth(
                    threeDot,
                    tvTitle.paint
                ).toInt()

            // 最后一行的起始序号和终止序号
            val textLayout: Layout = tvTitle.layout ?: return@post
            val lastLineStart = textLayout.getLineStart(contentLineCount)
            val lastLineEnd = textLayout.getLineEnd(contentLineCount)
            // 最后一行宽度
            val lastLineWidth = textLayout.getLineWidth(contentLineCount)
            if (lastLineWidth > realAvailableWidth) {
                var clipCharIndex = lastLineStart
                for (i in lastLineEnd - 1 downTo lastLineStart + 1) {
                    // 从最后一个字开始裁，一直裁到能放下为止
                    val afterClipText: CharSequence = contentText.subSequence(lastLineStart, i)
                    val afterClipWidth = Layout.getDesiredWidth(afterClipText, tvTitle.paint)
                    if (afterClipWidth <= realAvailableWidth) {
                        // 如果能放下则把序号记录下来
                        clipCharIndex = i
                        break
                    }
                }
                tvTitle.text = contentText.subSequence(0, clipCharIndex)
                if (contentLineCount >= maxLines) {
                    tvTitle.append(threeDot)
                }
            } else {
                if (lastLineEnd > 1 && contentText[lastLineEnd - 1] == '\n' && contentText[lastLineEnd - 2] == '\r') {
                    // 如果最后是换行符则把换行符也删去
                    tvTitle.text = contentText.subSequence(0, lastLineEnd - 2).toString()
                } else if (lastLineEnd > 0 && contentText[lastLineEnd - 1] == '\n') {
                    // 如果最后是换行符则把换行符也删去
                    tvTitle.text = contentText.subSequence(0, lastLineEnd - 1)
                } else {
                    tvTitle.text = contentText.subSequence(0, lastLineEnd).toString()
                }
                if (contentLineCount >= maxLines) {
                    tvTitle.append(threeDot)
                }
            }
        }

    }

}