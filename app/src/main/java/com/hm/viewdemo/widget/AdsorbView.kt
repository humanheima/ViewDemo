package com.hm.viewdemo.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.hm.viewdemo.databinding.ViewSelectedModeBinding
import com.hm.viewdemo.extension.dp2px
import com.hm.viewdemo.util.ScreenUtil
import kotlin.math.abs


/**
 * Created by p_dmweidu on 2025/3/29
 * Desc:
 */
class AdsorbView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val TAG = "AdsorbView"
    }


    private val defaultAnimationDuration = 300

    private val rect: Rect = Rect()
    private var downX = 0
    private var downY = 0
    private var lastX = 0
    private var lastY = 0

    /**
     * 可以拖动的区域
     */
    private var dragRect: Rect? = null

    /**
     * 是否设置了拖动区域
     */
    private var isSetDragRect = false

    /**
     * 最小拖动距离
     */
    private var mTouchSlop = 0
    private var interruptedMove = false
    private var isSuctionStatus: Boolean = false //当前是否是吸边状态
    private var mIsRightStatus = true //当前是否在右边
    private var isMoving = false

    private val moveDirections: MutableSet<IMoveDirection> = HashSet()

    private var binding: ViewSelectedModeBinding =
        ViewSelectedModeBinding.inflate(LayoutInflater.from(context), this)

    private var ivIcon: ImageView = binding.ivLeft
    private var tvText: TextView = binding.tvText

    init {
        initView()
    }

    private fun initView() {
        initRect()
        isClickable = true
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        Log.d(TAG, "initView: mTouchSlop = $mTouchSlop")
    }

    private fun initRect() {
        val screenWidth = ScreenUtil.getScreenWidth(context)
        val screenHeight = ScreenUtil.getScreenHeight(context)
        Log.i(TAG, "initRect: screenWidth$screenWidth , screenHeight = $screenHeight")
        dragRect = Rect(0, 0, screenWidth, screenHeight)

        Log.i(TAG, "initRect: drapRect = " + dragRect!!.flattenToString())
        post(Runnable {
            getHitRect(rect)
            if (isSetDragRect) {
                return@Runnable
            }
            if (parent is View) {
                (parent as View).getHitRect(dragRect)
                Log.i(TAG, "initRect: post drapRect = " + dragRect!!.flattenToString())
            }
        })
    }

    fun setLayoutId(layoutId: Int) {
        if (layoutId > 0) {
            removeAllViews()
            inflate(context, layoutId, this)
        }
    }

    fun setContentView(view: View?) {
        if (view == null) {
            return
        }
        removeAllViews()
        addView(view)
    }


    fun setDragRect(rect: Rect?) {
        isSetDragRect = true
        this.dragRect = rect
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (!isSetDragRect) {
            initRect()
        }
    }

    fun addMoveDirectionListener(moveDirection: IMoveDirection) {
        moveDirections.add(moveDirection)
    }

    fun removeDirectionListener(moveDirection: IMoveDirection) {
        moveDirections.remove(moveDirection)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //吸边时禁止拖曳
        //if (!isSuctionStatus) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //获取在父坐标系中的位置
                getHitRect(rect)
                downX = event.rawX.toInt()
                downY = event.rawY.toInt()
                lastX = event.rawX.toInt()
                lastY = event.rawY.toInt()
                isMoving = true
                Log.i(TAG, "onTouchEvent: ACTION_DOWN rect = " + rect.flattenToString())
                Log.d(
                    TAG,
                    "onTouchEvent: downX = $downX , downY = $downY , lastX = $lastX , lastY = $lastY"
                )
                requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                if (interruptedMove) {
                    return true
                }

                val moveX = event.rawX.toInt() - downX
                val moveY = event.rawY.toInt() - downY

                rect.offset(moveX, moveY)

                Log.i(TAG, "onTouchEvent: ACTION_MOVE rect = " + rect.flattenToString())

                x = rect.left.toFloat()
                y = rect.top.toFloat()

                downX = event.rawX.toInt()
                downY = event.rawY.toInt()
            }

            MotionEvent.ACTION_UP -> {
                isMoving = false
                //手指一动距离过小
                val hMoveDistance = lastX - event.rawX
                val vMoveDistance = lastY - event.rawY
                if (abs(hMoveDistance.toDouble()) < mTouchSlop && abs(vMoveDistance.toDouble()) < mTouchSlop) {
                    // Note: 2025/3/29 过小的话，直接回到原来的位置
                    Log.i(
                        TAG,
                        "onTouchEvent: 移动距离过小，直接回到原来的位置  hMoveDistance = $hMoveDistance , vMoveDistance = $vMoveDistance"
                    )
                    rect.offset(hMoveDistance.toInt(), vMoveDistance.toInt())
                    x = rect.left.toFloat()
                    y = rect.top.toFloat()
                    return super.onTouchEvent(event)
                } else {
                    if (interruptedMove) {
                        return true
                    }
                }

                if (!interruptedMove) {
                    //AdsorbView的中心横坐标和可拖动区域中心横坐标比较，x<0靠左，x>0靠右
                    val x = rect.centerX() - ((dragRect!!.right - dragRect!!.left) / 2)
                    if (x < 0) {
                        for (moveDirection in moveDirections) {
                            moveDirection.moveDirection(false)
                        }
                        //靠左，rect的left坐标设置为0
                        rect.offsetTo(0, rect.top)
                        mIsRightStatus = false
                    } else {
                        for (moveDirection in moveDirections) {
                            moveDirection.moveDirection(true)
                        }
                        //靠右，rect的left坐标设置为drapRect的宽度减去AdsorbView的的宽度
                        rect.offsetTo(dragRect!!.right - dragRect!!.left - width, rect.top)
                        mIsRightStatus = true
                    }

                    if (rect.top < dragRect!!.top) {
                        Log.d(
                            TAG,
                            "onTouchEvent: rect.top = ${rect.top} , dragRect.top = ${dragRect?.top}"
                        )
                        //不能超过上边界
                        rect.offsetTo(rect.left, dragRect!!.top)
                    } else if (rect.bottom > dragRect!!.bottom) {
                        //不能超过下边界
                        rect.offsetTo(rect.left, dragRect!!.bottom - height)
                    }
                    startFlingAnim()
                    requestDisallowInterceptTouchEvent(false)
                    return true
                }
                isMoving = false
                requestDisallowInterceptTouchEvent(false)
            }

            MotionEvent.ACTION_CANCEL -> {
                isMoving = false
                requestDisallowInterceptTouchEvent(false)
            }

            else -> {}
        }
        //}
        return super.onTouchEvent(event)
    }

    fun setInterruptedMove(interrupted: Boolean) {
        this.interruptedMove = interrupted
    }

    /**
     * 开始惯性滑动动画
     */
    private fun startFlingAnim() {
        if (dragRect == null) {
            return
        }

        swapViews()
        Log.d(TAG, "startFlingAnim: x = $x , y = $y")
        Log.d(TAG, "startFlingAnim: rect = " + rect.flattenToString())
        Log.d(
            TAG,
            "startFlingAnim: dragRect = " + dragRect?.flattenToString() + " centerX = " + dragRect?.centerX() + " centerY = " + dragRect?.centerY()
        )
        val animatorSet = AnimatorSet()
        val objectAnimator1 = ObjectAnimator.ofFloat(this, "x", x, rect.left.toFloat())
        val objectAnimator2 = ObjectAnimator.ofFloat(this, "y", y, rect.top.toFloat())
        animatorSet.playTogether(objectAnimator1, objectAnimator2)
        /**
         * rect.left：down事件的时候，在父布局中的位置
         * x：当前的位置
         * 计算动画时间，横向距离大于纵向距离，是用横向距离类似计算。
         *
         */
        if (abs(x - rect.left) > abs(y - rect.top)) {
            val centerX = dragRect?.centerX() ?: 0
            if (centerX != 0) {
                var v = defaultAnimationDuration * 1.0f / centerX * abs(x - rect.left)
                if (v <= 0) {
                    v = 50f
                }
                Log.e(TAG, "startFlingAnim:水平方向 v = $v")
                animatorSet.setDuration(v.toLong())
            }
        } else {
            val centerY = dragRect?.centerY() ?: 0
            if (centerY != 0) {
                var v = defaultAnimationDuration * 1.0f / centerY * abs(y - rect.top)
                if (v <= 0) {
                    v = 50f
                }
                Log.e(TAG, "startFlingAnim:垂直方向 v = $v")
                animatorSet.setDuration(v.toLong())
            }
        }

        animatorSet.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                Log.d(TAG, "onAnimationEnd: ")
                removeCallbacks(autoHidePartRunnable)
                postDelayed(autoHidePartRunnable, 2000)
            }

        })
        animatorSet.start()
    }

    /**
     * 交换两个View的位置
     */
    private fun swapViews() {
        val leftChild = getChildAt(0) ?: return
        val rightChild = getChildAt(1) ?: return
        if (mIsRightStatus) {
            //在右边
            if (leftChild is TextView) {
                removeAllViews()

                //添加Icon
                var lp = LayoutParams(
                    16.dp2px(context),
                    16.dp2px(context)
                )
                lp.marginStart = 10.dp2px(context)
                addView(rightChild, lp)

                //添加TextView
                lp = LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                )
                lp.marginStart = 4.dp2px(context)
                lp.marginEnd = 22.dp2px(context)
                addView(leftChild, lp)
            }
        } else {
            //在左边
            if (rightChild is TextView) {
                removeAllViews()

                //添加TextView
                var lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                lp.marginStart = 22.dp2px(context)
                lp.marginEnd = 4.dp2px(context)
                addView(rightChild, lp)

                //添加Icon
                lp = LayoutParams(
                    16.dp2px(context),
                    16.dp2px(context)
                )
                lp.marginEnd = 10.dp2px(context)
                addView(leftChild, lp)
            }
        }
    }

    private var autoHidePartRunnable = Runnable {
        autoHidePart(width - 30.dp2px(context))
    }

    /**
     * 设置初始化吸边
     * @param isSuction 是否要设置吸边
     * @param horizontalOffset 吸边偏移的像素
     */
    fun setInitialSuction(isSuction: Boolean, horizontalOffset: Int = 0) {
        if (isSuctionStatus == isSuction || interruptedMove || isMoving || dragRect == null) {
            return
        }
        if (isSuction) {
            //吸边操作
            if (mIsRightStatus) {
                //右吸边
                val newLeft = dragRect!!.right - dragRect!!.left - width + horizontalOffset
                rect.offsetTo(newLeft, rect.top)
            } else {
                //左吸边
                rect.offsetTo(-horizontalOffset, rect.top)
            }
        } else {
            //取消吸边操作
            if (mIsRightStatus) {
                rect.offsetTo(dragRect!!.right - dragRect!!.left - width, rect.top)
            } else {
                rect.offsetTo(0, rect.top)
            }
        }

        //startFlingAnim()
        x = rect.left.toFloat()
        y = rect.top.toFloat()

        isSuctionStatus = isSuction
    }

    /**
     * 靠边后，自动隐藏部分
     * @param horizontalOffset 隐藏的的像素
     */
    private fun autoHidePart(horizontalOffset: Int = 0) {
        if (interruptedMove || isMoving || isAttachedToWindow.not()) {
            return
        }

        Log.e(TAG, "autoHidePart: x = $x , y = $y")
        Log.e(TAG, "autoHidePart: rect = " + rect.flattenToString())
        Log.e(
            TAG,
            "autoHidePart: dragRect = " + dragRect?.flattenToString() + " centerX = " + dragRect?.centerX() + " centerY = " + dragRect?.centerY()
        )

        val finalX = if (mIsRightStatus) {
            //右吸边
            dragRect!!.right - dragRect!!.left - width + horizontalOffset
            //rect.offsetTo(newLeft, rect.top)
        } else {
            //左吸边
            //rect.offsetTo(-horizontalOffset, rect.top)
            -horizontalOffset
        }
        val animatorSet = AnimatorSet()
        val objectAnimator1 = ObjectAnimator.ofFloat(this, "x", x, finalX.toFloat())
        animatorSet.playTogether(objectAnimator1)
        animatorSet.setDuration(200)
        animatorSet.start()
    }

    interface IMoveDirection {
        fun moveDirection(isRight: Boolean)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        moveDirections.clear()
    }


    /**
     * 获取View距拖动范围底部的距离
     */
    fun getBottomMargin(): Int {
        Log.i(TAG, "drapRect.bottom=" + dragRect!!.bottom)
        Log.i(TAG, "drapRect.top=" + dragRect!!.top)
        Log.i(TAG, "drapRect.left=" + dragRect!!.left)
        Log.i(TAG, "drapRect.right=" + dragRect!!.right)
        Log.i(TAG, "drapRect.高度=" + (dragRect!!.bottom - dragRect!!.top))

        Log.i(TAG, "===========")

        Log.i(TAG, "rect.bottom=" + rect.bottom)
        Log.i(TAG, "rect.top=" + rect.top)
        Log.i(TAG, "rect.left=" + rect.left)
        Log.i(TAG, "rect.right=" + rect.right)
        Log.i(TAG, "rect.高度=" + (rect.bottom - rect.top))

        Log.i(TAG, "===========")
        Log.i(TAG, "bottom差=" + (dragRect!!.bottom - rect.bottom))
        Log.i(TAG, "top差=" + rect.top)

        //计算出
        val height = rect.bottom - rect.top
        if (height == 0) {
            return 0
        }
        //处于可拖动区域的最顶部
        if (rect.top <= 0) {
            return dragRect!!.bottom - height
        }
        //处于可拖动区域的最底部
        if (rect.bottom >= dragRect!!.bottom) {
            return 0
        }
        //在正确范围内，可拖动区域的底部减去AdsorbView的底部就是bottomMargin
        return dragRect!!.bottom - rect.bottom
    }


}
