package com.hm.viewdemo.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2025/2/21
 * Desc: 最后测量的需要被压缩的 View 用来实现LinearLayout从右向左排列，控件不足时挤压左边。
 * 原理: LinearLayout原本是优先测量左边的，然后再测量右边的，当空间不足时，右边的View就被挤压了。 若重写LinearLayout的onMeasure，使其顺序反过来，那么就解决了。
 * 参考链接： https://juejin.cn/post/7234060427411849271
 */
class FinalMeasureLinearlayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayoutCompat(context, attrs) {

    companion object {
        private const val TAG = "FinalMeasureLinearlayou"

        private const val FINAL_MEASURE_INDEX_INVALID = -1
    }

    private var finalMeasureIndex = FINAL_MEASURE_INDEX_INVALID

    override fun getChildAt(index: Int): View {
        if (finalMeasureIndex < 0) {
            //非测量期间
            return super.getChildAt(index)
        }
        if (index < finalMeasureIndex) {
            //可以先测量的部分
            return super.getChildAt(index)
        }
        return if (index == childCount - 1) {
            //最后测量的View
            super.getChildAt(finalMeasureIndex)
        } else {
            //跳过最后测量的View
            super.getChildAt(index + 1)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val count = childCount
        finalMeasureIndex = FINAL_MEASURE_INDEX_INVALID
        for (i in 0 until count) {
            if (isFinalMeasure(getChildAt(i))) {
                finalMeasureIndex = i
                Log.d(TAG, "onMeasure: finalMeasureIndex = $finalMeasureIndex")
                break
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        finalMeasureIndex = FINAL_MEASURE_INDEX_INVALID
    }

    /**
     * 根据
     */
    private fun isFinalMeasure(view: View): Boolean {
        val layoutParams = view.layoutParams
        if (layoutParams is LayoutParams) {
            return layoutParams.finalMeasure
        }
        return false
    }

    override fun generateLayoutParams(p: ViewGroup.LayoutParams): LayoutParams {
        return LayoutParams(p)
    }

    override fun generateDefaultLayoutParams(): LayoutParams? {
        if (orientation == HORIZONTAL) {
            return LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
            )
        } else if (orientation == VERTICAL) {
            return LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT
            )
        }
        return null
    }

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        return LayoutParams(context, attrs)
    }

    override fun checkLayoutParams(p: ViewGroup.LayoutParams): Boolean {
        return p is LayoutParams
    }

    class LayoutParams : LinearLayoutCompat.LayoutParams {
        var finalMeasure: Boolean = false

        constructor(c: Context, attrs: AttributeSet?) : super(c, attrs) {
            val typedArray =
                c.obtainStyledAttributes(attrs, R.styleable.FinalMeasureLinearlayout_Layout)
            finalMeasure = typedArray.getBoolean(
                R.styleable.FinalMeasureLinearlayout_Layout_layout_final_measure,
                false
            )
            typedArray.recycle()
        }

        constructor(width: Int, height: Int) : super(width, height)

        constructor(width: Int, height: Int, weight: Float) : super(width, height, weight)

        constructor(p: ViewGroup.LayoutParams?) : super(p)

        constructor(source: MarginLayoutParams?) : super(source)
    }

}
