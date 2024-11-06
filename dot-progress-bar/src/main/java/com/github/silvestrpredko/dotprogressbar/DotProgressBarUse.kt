package com.github.silvestrpredko.dotprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.Transformation
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat

/**
 * @author Silvestr Predko.
 */
class DotProgressBarUse @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    companion object {
        const val RIGHT_DIRECTION: Int = 1
        const val LEFT_DIRECTION: Int = -1
    }


    var enableAnimation = true

    var debugDrawBiggest = false

    /**
     * Dots amount
     */
    private var dotAmount = 0

    /**
     * Drawing tools
     */
    private var primaryPaint: Paint? = null

    /**
     * Animation tools
     */
    private var animationTime: Long = 0
    private var animatedRadius = 0f
    private var isFirstLaunch = true

    /**
     * Circle size
     */
    private var dotRadius = 0f
    private var bounceDotRadius = 0f

    /**
     * Circle coordinates
     * 第一个圆的坐标
     */
    private var xCoordinate = 0f
    private var dotPosition = 0

    /**
     * Colors
     */
    private var dotColor = 0

    /**
     * This value detect direction of circle animation direction
     * [DotProgressBarUse.RIGHT_DIRECTION] and [DotProgressBarUse.LEFT_DIRECTION]
     */
    var animationDirection: Int = 0

    init {
        initializeAttributes(attrs)
        initPaint()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredHeight)
        dotRadius = if (measuredHeight > measuredWidth) {
            measuredWidth.toFloat() / dotAmount / 4
        } else {
            measuredHeight.toFloat() / 4
        }

        bounceDotRadius = dotRadius + (dotRadius / 3)
        val circlesWidth = (dotAmount * (dotRadius * 2)) + dotRadius * (dotAmount - 1)
        xCoordinate = (measuredWidth - circlesWidth) / 2 + dotRadius
    }

    private fun initializeAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.DotProgressBar)
            try {
                setDotAmount(a.getInteger(R.styleable.DotProgressBar_amount, 5))
                setAnimationTime(
                    a.getInteger(
                        R.styleable.DotProgressBar_duration,
                        resources.getInteger(android.R.integer.config_mediumAnimTime)
                    ).also { animationTime = it.toLong() }.toLong()
                )
                setDotColor(
                    a.getInteger(
                        R.styleable.DotProgressBar_startColor,
                        ContextCompat.getColor(context, R.color.light_blue_A700)
                    )
                )
                animationDirection = a.getInt(R.styleable.DotProgressBar_animationDirection, 1)
            } finally {
                a.recycle()
            }
        } else {
            setDotAmount(5)
            setAnimationTime(resources.getInteger(android.R.integer.config_mediumAnimTime).toLong())
            setDotColor(ContextCompat.getColor(context, R.color.light_blue_A700))
            animationDirection = 1
        }
    }

    private fun initPaint() {
        primaryPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
        primaryPaint?.color = dotColor
        primaryPaint?.strokeJoin = Paint.Join.ROUND
        primaryPaint?.strokeCap = Paint.Cap.ROUND
        primaryPaint?.strokeWidth = 20f
    }

    /**
     * setters
     */
    fun setDotAmount(amount: Int) {
        this.dotAmount = amount
    }

    fun setDotColor(@ColorInt color: Int) {
        this.dotColor = color
    }

    fun setAnimationTime(animationTime: Long) {
        this.animationTime = animationTime
    }

    private fun setDotPosition(dotPosition: Int) {
        this.dotPosition = dotPosition
    }

    /**
     * Set amount of dots, it will be restarted your view
     *
     * @param amount number of dots, dot size automatically adjust
     */
    fun changeDotAmount(amount: Int) {
        stopAnimation()
        setDotAmount(amount)
        setDotPosition(0)
        reinitialize()
    }

    /**
     * It will be restarted your view
     */
    fun changeColor(@ColorInt color: Int) {
        stopAnimation()
        setDotColor(color)
        reinitialize()
    }

    /**
     * It will be restarted your view
     */
    fun changeAnimationTime(animationTime: Long) {
        stopAnimation()
        setAnimationTime(animationTime)
        reinitialize()
    }

    /**
     * Change animation direction, doesn't restart view.
     *
     * @param animationDirection left or right animation direction
     */
    fun changeAnimationDirection(@AnimationDirection animationDirection: Int) {
        this.animationDirection = animationDirection
    }

    /**
     * Reinitialize animators instances
     */
    fun reinitialize() {
        initPaint()
        requestLayout()
        startAnimation()
    }

    private val TAG = "DotProgressBarUse"

    private fun drawCirclesLeftToRight(canvas: Canvas, radius: Float) {
        Log.d(TAG, "drawCirclesLeftToRight: ")
        var step = 0f

        var pointStep = 0f
        for (i in 0 until dotAmount) {

            drawCircles(canvas, i, step, radius)

            //val flx = xCoordinate + pointStep

            //确定圆心位置
            val flx = xCoordinate + step
            val fry = measuredHeight.toFloat() / 2
            Log.d(TAG, "drawCirclesLeftToRight: flx = $flx, fry = $fry")
            canvas.drawPoint(flx, fry, paint)

            step += dotRadius * 3
            pointStep += dotRadius
        }
    }

    private fun drawCirclesRightToLeft(canvas: Canvas, radius: Float) {
        Log.d(TAG, "drawCirclesRightToLeft: ")
        var step = 0f
        for (i in dotAmount - 1 downTo 0) {
            drawCircles(canvas, i, step, radius)
            step += dotRadius * 3
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL

        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 20f

    }

    private fun drawCircles(canvas: Canvas, i: Int, step: Float, radius: Float) {
        if (dotPosition == i) {
            drawCircleUp(canvas, step, radius)
        } else {
            if ((i == (dotAmount - 1) && dotPosition == 0 && !isFirstLaunch) || ((dotPosition - 1) == i)) {
                drawCircleDown(canvas, step, radius)
            } else {
                drawCircle(canvas, step)
            }
        }
    }

    /**
     * Circle radius is grow
     */
    private fun drawCircleUp(canvas: Canvas, step: Float, radius: Float) {
        val finalRadius = if (debugDrawBiggest) {
            bounceDotRadius
        } else {
            dotRadius + radius
        }
        canvas.drawCircle(
            xCoordinate + step,
            measuredHeight.toFloat() / 2,
            //dotRadius + radius,
            finalRadius,
            primaryPaint!!
        )
    }

    private fun drawCircle(canvas: Canvas, step: Float) {

        val finalRadius = if (debugDrawBiggest) {
            bounceDotRadius
        } else {
            dotRadius
        }

        canvas.drawCircle(
            xCoordinate + step,
            measuredHeight.toFloat() / 2,
            //dotRadius,
            finalRadius,
            primaryPaint!!
        )
    }

    /**
     * Circle radius is decrease
     */
    private fun drawCircleDown(canvas: Canvas, step: Float, radius: Float) {
        val finalRadius = if (debugDrawBiggest) {
            bounceDotRadius
        } else {
            bounceDotRadius - radius
        }

        canvas.drawCircle(
            xCoordinate + step,
            measuredHeight.toFloat() / 2,
            //bounceDotRadius - radius,
            finalRadius,
            primaryPaint!!
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.DKGRAY)
        if (animationDirection < 0) {
            drawCirclesRightToLeft(canvas, animatedRadius)
        } else {
            drawCirclesLeftToRight(canvas, animatedRadius)
        }
    }

    private fun stopAnimation() {
        this.clearAnimation()
        postInvalidate()
    }

    private fun startAnimation() {
        if (!enableAnimation) {
            return
        }
        val bounceAnimation: BounceAnimation = BounceAnimation()
        bounceAnimation.duration = animationTime
        bounceAnimation.repeatCount = Animation.INFINITE
        bounceAnimation.interpolator = LinearInterpolator()
        bounceAnimation.setAnimationListener(object : AnimationListener() {
            override fun onAnimationRepeat(animation: Animation) {
                dotPosition++
                if (dotPosition == dotAmount) {
                    dotPosition = 0
                }
                isFirstLaunch = false
            }
        })
        startAnimation(bounceAnimation)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)

        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation()
        } else {
            startAnimation()
        }
    }

    override fun onDetachedFromWindow() {
        stopAnimation()
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(*[RIGHT_DIRECTION, LEFT_DIRECTION])
    annotation class AnimationDirection

    private inner class BounceAnimation : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            super.applyTransformation(interpolatedTime, t)
            animatedRadius = (bounceDotRadius - dotRadius) * interpolatedTime
            invalidate()
        }
    }

}