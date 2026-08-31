package com.hm.viewdemo.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.animation.DecelerateInterpolator
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.math.sign

/**
 * A compact wheel picker that keeps five values visible at a time.
 * The center value is selected; dragging or flinging changes the value.
 */
class FiveItemWheelPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        private const val VISIBLE_ITEM_COUNT = 5
        private const val HALF_VISIBLE_ITEM_COUNT = VISIBLE_ITEM_COUNT / 2
        private const val ROW_HEIGHT_DP = 48f
        private const val MIN_FLING_VELOCITY = 900f
    }

    private val rowHeight = ROW_HEIGHT_DP * resources.displayMetrics.density
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT
    }

    private var scrollOffset = 0f
    private var lastTouchY = 0f
    private var velocityTracker: VelocityTracker? = null
    private var settleAnimator: ValueAnimator? = null

    var minValue: Int = 0
        set(value) {
            field = value
            this.value = normalize(this.value)
            invalidate()
        }

    var maxValue: Int = 100
        set(value) {
            field = value.coerceAtLeast(minValue)
            this.value = normalize(this.value)
            invalidate()
        }

    var value: Int = 0
        set(newValue) {
            val normalizedValue = normalize(newValue)
            val oldValue = field
            field = normalizedValue
            invalidate()
            if (oldValue != normalizedValue) {
                onValueChangedListener?.invoke(this, oldValue, normalizedValue)
            }
        }

    var wrapSelectorWheel: Boolean = false
        set(value) {
            field = value
            this.value = normalize(this.value)
            invalidate()
        }

    var formatter: (Int) -> String = { it.toString() }
        set(value) {
            field = value
            invalidate()
        }

    private var onValueChangedListener: ((FiveItemWheelPicker, Int, Int) -> Unit)? = null

    fun setOnValueChangedListener(listener: ((FiveItemWheelPicker, Int, Int) -> Unit)?) {
        onValueChangedListener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minimumWidth = (96 * resources.displayMetrics.density).roundToInt()
        val preferredHeight = (rowHeight * VISIBLE_ITEM_COUNT).roundToInt()
        setMeasuredDimension(
            resolveSize(minimumWidth, widthMeasureSpec),
            resolveSize(preferredHeight, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        for (offsetIndex in -HALF_VISIBLE_ITEM_COUNT - 1..HALF_VISIBLE_ITEM_COUNT + 1) {
            val itemY = centerY + offsetIndex * rowHeight - scrollOffset
            if (itemY < -rowHeight || itemY > height + rowHeight) {
                continue
            }

            val itemValue = valueForOffset(offsetIndex)
            if (itemValue == null) {
                continue
            }

            val distance = abs(itemY - centerY) / rowHeight
            textPaint.color = if (distance < 0.5f) Color.BLACK else Color.DKGRAY
            textPaint.alpha = (255 - (distance * 65).roundToInt()).coerceIn(90, 255)
            textPaint.textSize = spToPx(if (distance < 0.5f) 20f else 18f)
            val baseline = itemY - (textPaint.ascent() + textPaint.descent()) / 2f
            canvas.drawText(formatter(itemValue), centerX, baseline, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                settleAnimator?.cancel()
                lastTouchY = event.y
                velocityTracker?.recycle()
                velocityTracker = VelocityTracker.obtain().also { it.addMovement(event) }
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaY = event.y - lastTouchY
                scrollOffset = (scrollOffset - deltaY).coerceIn(-rowHeight * 10, rowHeight * 10)
                lastTouchY = event.y
                velocityTracker?.addMovement(event)
                invalidate()
                return true
            }

            MotionEvent.ACTION_UP -> {
                velocityTracker?.addMovement(event)
                velocityTracker?.computeCurrentVelocity(1000)
                settle(velocityTracker?.yVelocity ?: 0f)
                velocityTracker?.recycle()
                velocityTracker = null
                parent.requestDisallowInterceptTouchEvent(false)
                performClick()
                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                settle(0f)
                velocityTracker?.recycle()
                velocityTracker = null
                parent.requestDisallowInterceptTouchEvent(false)
                return true
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private fun settle(yVelocity: Float) {
        val draggedSteps = (scrollOffset / rowHeight).roundToInt()
        val flingSteps = when {
            yVelocity < -MIN_FLING_VELOCITY -> 2
            yVelocity > MIN_FLING_VELOCITY -> -2
            else -> 0
        }
        var targetSteps = draggedSteps + flingSteps
        if (targetSteps == 0 && abs(scrollOffset) > rowHeight * 0.2f) {
            targetSteps = scrollOffset.sign.toInt()
        }
        targetSteps = targetSteps.coerceIn(-10, 10)

        val startOffset = scrollOffset
        val endOffset = targetSteps * rowHeight
        settleAnimator = ValueAnimator.ofFloat(startOffset, endOffset).apply {
            duration = 180L
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                scrollOffset = animation.animatedValue as Float
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    if (settleAnimator === animation) {
                        scrollOffset = 0f
                        if (targetSteps != 0) {
                            value = value + targetSteps
                        }
                        settleAnimator = null
                        invalidate()
                    }
                }
            })
            start()
        }
    }

    private fun valueForOffset(offsetIndex: Int): Int? {
        val candidate = value + offsetIndex
        if (wrapSelectorWheel) {
            return normalize(candidate)
        }
        return candidate.takeIf { it in minValue..maxValue }
    }

    private fun normalize(candidate: Int): Int {
        val range = maxValue - minValue + 1
        if (range <= 0) {
            return minValue
        }
        if (!wrapSelectorWheel) {
            return candidate.coerceIn(minValue, maxValue)
        }
        return ((candidate - minValue) % range + range) % range + minValue
    }

    private fun spToPx(sp: Float): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        resources.displayMetrics
    )
}
