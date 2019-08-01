package com.hm.viewdemo.widget

import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.hm.viewdemo.R
import com.hm.viewdemo.activity.CountDownTimerActivity
import kotlinx.android.synthetic.main.count_down_view.view.*

/**
 * Created by dumingwei on 2019-07-31.
 * Desc:
 */
class CountDownView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val TAG = "CountDownView"

    private var countDownTimer: CountDownTimer?=null

    private var millisInFuture = 0L

    private var interval = INTERVAL

    var countDownFinishAction: (() -> Unit)? = null

    companion object {

        const val HOUR = 3600
        const val MINUTE = 60
        //倒计时时间1秒
        const val INTERVAL = 1000L

    }

    init {
        View.inflate(getContext(), R.layout.count_down_view, this)
    }

    /**
     * @param finishTime 毫秒
     */
    fun initData(finishTime: Long) {
        /* if (finishTime < 0 || finishTime < interval) {
             //这里应该隐藏倒计时控件，显示一个已结束的控件
             Log.d(TAG, "initData: ")
             return
         }*/

        this.millisInFuture = finishTime - System.currentTimeMillis()
    }

    /**
     *
     */
    fun start() {
        if (millisInFuture < interval) {
            //如果剩余时间小于间隔时间
            tvHour.text = String.format("%02d", 0)
            tvMinute.text = String.format("%02d", 0)
            tvSecond.text = String.format("%02d", 0)
            countDownFinishAction?.invoke()
        } else {
            countDownTimer?.cancel()
            countDownTimer = object : CountDownTimer(millisInFuture, interval) {

                override fun onTick(millisUntilFinished: Long) {

                    val second = millisUntilFinished / 1000

                    val hour = second / CountDownTimerActivity.HOUR
                    val minute = second % CountDownTimerActivity.HOUR / CountDownTimerActivity.MINUTE
                    val s = second % 60

                    tvHour.text = String.format("%02d", hour)
                    tvMinute.text = String.format("%02d", minute)
                    tvSecond.text = String.format("%02d", s)

                }

                override fun onFinish() {
                    tvHour.text = String.format("%02d", 0)
                    tvMinute.text = String.format("%02d", 0)
                    tvSecond.text = String.format("%02d", 0)
                    countDownFinishAction?.invoke()
                }
            }
            countDownTimer?.start()
        }

    }
}