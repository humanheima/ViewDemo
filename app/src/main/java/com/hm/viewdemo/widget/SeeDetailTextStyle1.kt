package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ScreenUtil

/**
 * Created by p_dmweidu on 2023/10/18
 * Desc: TextView + 查看详情，查看详情固定在末尾，不会紧跟...
 *
 * 放弃
 */
class SeeDetailTextStyle1 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RelativeLayout(context, attrs) {

    private val TAG: String = "SeeDetailTextStyle1"

    private var tvTitle: TextView? = null
    private var tvSeeDetail: TextView? = null

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

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.layout_see_detail_textview, this)
        tvTitle = findViewById(R.id.tv_child_tab_title)
        tvSeeDetail = findViewById(R.id.tv_see_detail)

        widthOfEllipsis = tvTitle?.paint?.measureText("...")?.toInt() ?: 0
        oneChineseWidth = tvTitle?.paint?.measureText(oneChinese)?.toInt() ?: 0

        contentWidth = ScreenUtil.getScreenWidth(context)
        suffixWidth = tvSeeDetail?.paint?.measureText(tvSeeDetail?.text.toString())?.toInt() ?: 0
        Log.i(
            TAG,
            "initView: suffixWidth = $suffixWidth tvVoiceContentWidth = $contentWidth"
        )
        oneLineWidth = contentWidth - suffixWidth
        Log.i(TAG, "initView: oneLineWidth = $oneLineWidth")
        twoLineWidth = (contentWidth * 2) - suffixWidth - oneChineseWidth
        Log.i(TAG, "initView: twoLineWidth = $twoLineWidth")
        threeLineWidth = (contentWidth * 3) - suffixWidth - 2 * oneChineseWidth
        Log.i(TAG, "initView: threeLineWidth = $threeLineWidth")



        threeLineWidthSubtractWidthOfEllipsis = threeLineWidth - widthOfEllipsis
    }

    fun setText(titleText: String?) {
        if (titleText.isNullOrEmpty()) {
            tvTitle?.text = ""
            return
        }
        val text: String = titleText
        val measureTextWidth = tvTitle?.paint?.measureText(text)?.toInt() ?: 0

        if (measureTextWidth <= oneLineWidth) {
            tvTitle?.text = text
            return
        }

        //顶到详情的位置了，换行
        if (measureTextWidth <= contentWidth) {
            tvTitle?.text = "$text\n"
            return
        }

        //加一个换行的高度
        val measureTextWidthAddOneReturnWidth = measureTextWidth + oneChineseWidth
        if (measureTextWidthAddOneReturnWidth <= twoLineWidth) {
            tvTitle?.text = text
            return
        }
        if (measureTextWidthAddOneReturnWidth <= (contentWidth * 2)) {
            tvTitle?.text = "$text\n"
            return
        }
        //加2个换行的高度
        val measureTextWidthAddTwoReturnWidth = measureTextWidth + oneChineseWidth + oneChineseWidth

        if (measureTextWidthAddTwoReturnWidth <= threeLineWidth) {
            tvTitle?.text = text
            return
        }
        // 三行
        tvTitle?.text = getVoiceTextWithSuffix(text)
    }

    private fun getVoiceTextWithSuffix(text: String?): String {
        if (text.isNullOrEmpty()) {
            return ""
        }
        val textLength = text.length
        val start = System.currentTimeMillis()
        if (textLength > 20) {
            Log.i(TAG, "需要截断 textLength > 20 start at start")
            var tmpWidth = 0f
            var lastCharIndex = -1
            for (index in 20..textLength) {
                tmpWidth = tvTitle?.paint?.measureText(text, 0, index) ?: 0f
                if (tmpWidth >= threeLineWidthSubtractWidthOfEllipsis) {
                    lastCharIndex = index
                    Log.i(TAG, "lastCharIndex = $lastCharIndex")
                    break
                }
            }
            return if (lastCharIndex >= 20) {
                Log.i(
                    TAG,
                    "需要截断 lastCharIndex > 20 end 耗时 ${System.currentTimeMillis() - start} ms"
                )
                "${text.subSequence(0, lastCharIndex - 2)}..."
            } else {
                //正常不会走到这里的
                Log.i(TAG, "需要截断音 end 耗时 ${System.currentTimeMillis() - start} ms")
                ""
            }
        } else {
            //正常不会走到这里的
            return ""
        }
    }

}