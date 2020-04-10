package com.hm.viewdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_round_view.*
import java.lang.ref.WeakReference

/**
 * Created by dumingwei on 2020/4/2
 *
 * Desc: 测试圆角图片相关的东西
 */
class RoundViewActivity : AppCompatActivity() {


    private val TAG = "RoundViewActivity"

    private lateinit var handler: MyHandler

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, RoundViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var stageTime = 1000 * 1000

    private var timeToArriveThisStage = 1000 * 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_view)

        handler = MyHandler(this)

        btnStartCountdown.setOnClickListener {
            roundView.setInitialTimes(stageTime, timeToArriveThisStage)
            startCountdown()
        }
    }

    private fun startCountdown() {
        if (!roundView.finished()) {
            timeToArriveThisStage -= 1000
            //roundView.countDown(timeToArriveThisStage)
            roundView.setInitialTimes(stageTime, timeToArriveThisStage)
            Log.d(TAG, "sendEmptyMessageDelayed")
            handler.sendEmptyMessageDelayed(1, 100)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private class MyHandler(activity: RoundViewActivity) : Handler() {

        private val weakActivity: WeakReference<RoundViewActivity> = WeakReference(activity)

        override fun handleMessage(msg: Message) {
            val activity = weakActivity.get() as RoundViewActivity?
            activity?.startCountdown()
        }
    }

}
