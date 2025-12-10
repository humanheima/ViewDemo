package com.hm.viewdemo.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.hm.viewdemo.R
import com.hm.viewdemo.dp2px

/**
 * 自定义页面指示器
 * 支持跟随ViewPager滑动
 */
class CustomPagerIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_DOT_MARGIN = 16f // 默认点间距
    }

    private var selectedDrawable: Drawable?
    private var unselectedDrawable: Drawable?
    private var dotMargin: Float = DEFAULT_DOT_MARGIN
    private var pageCount: Int = 0
    private var currentPosition: Int = 0

    val selectedWidth = 16.dp2px(context)
    val selectedHeight = 6.dp2px(context)
    val unselectedWidth = 6.dp2px(context)
    val unselectedHeight = 6.dp2px(context)

    init {
        selectedDrawable = ContextCompat.getDrawable(context, R.drawable.icon_poster_select_dots)
        unselectedDrawable =
            ContextCompat.getDrawable(context, R.drawable.icon_poster_unselect_dots)

        // 从attributes中获取自定义属性（如果有的话）
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.CustomPagerIndicator, 0, 0)
            dotMargin = typedArray.getDimension(
                R.styleable.CustomPagerIndicator_dotMargin,
                DEFAULT_DOT_MARGIN
            )
            typedArray.recycle()
        }
    }

    /**
     * 设置页面数量
     */
    fun setPageCount(count: Int) {
        if (pageCount != count) {
            pageCount = count
            currentPosition = 0
            requestLayout()
            invalidate()
        }
    }

    /**
     * 设置当前页面位置
     */
    fun setCurrentPosition(position: Int) {
        if (position < 0 || position >= pageCount) return

        currentPosition = position
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (pageCount == 0) {
            setMeasuredDimension(0, 0)
            return
        }

//        val selectedWidth = selectedDrawable?.intrinsicWidth ?: 0
//        val selectedHeight = selectedDrawable?.intrinsicHeight ?: 0
//        val unselectedWidth = unselectedDrawable?.intrinsicWidth ?: 0
//        val unselectedHeight = unselectedDrawable?.intrinsicHeight ?: 0





        val maxWidth = maxOf(selectedWidth, unselectedWidth)
        val maxHeight = maxOf(selectedHeight, unselectedHeight)

        val totalWidth = (maxWidth * pageCount + dotMargin * (pageCount - 1)).toInt()
        val totalHeight = maxHeight

        val width = resolveSize(totalWidth + paddingLeft + paddingRight, widthMeasureSpec)
        val height = resolveSize(totalHeight + paddingTop + paddingBottom, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (pageCount == 0) return

        val selectedDrawable = this.selectedDrawable ?: return
        val unselectedDrawable = this.unselectedDrawable ?: return

        val maxWidth = maxOf(selectedWidth, unselectedWidth)
        val maxHeight = maxOf(selectedHeight, unselectedHeight)

        val totalContentWidth = maxWidth * pageCount + dotMargin * (pageCount - 1)
        val startX = (width - totalContentWidth) / 2f
        val centerY = height / 2f

        for (i in 0 until pageCount) {
            val dotCenterX = startX + i * (maxWidth + dotMargin) + maxWidth / 2f

            val drawable: Drawable
            val dotWidth: Int
            val dotHeight: Int

            // 简单判断：只有当前页面显示选中状态
            if (i == currentPosition) {
                drawable = selectedDrawable
                dotWidth = selectedWidth
                dotHeight = selectedHeight
            } else {
                drawable = unselectedDrawable
                dotWidth = unselectedWidth
                dotHeight = unselectedHeight
            }

            val left = (dotCenterX - dotWidth / 2f).toInt()
            val top = (centerY - dotHeight / 2f).toInt()
            val right = left + dotWidth
            val bottom = top + dotHeight

            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
    }

    /**
     * 绑定到ViewPager
     */
    fun attachToViewPager(viewPager: ViewPager) {
        setPageCount(viewPager.adapter?.count ?: 0)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // 不需要跟随滑动
            }

            override fun onPageSelected(position: Int) {
                setCurrentPosition(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                // 页面滚动状态改变时的逻辑可以在这里处理
            }
        })
    }
}