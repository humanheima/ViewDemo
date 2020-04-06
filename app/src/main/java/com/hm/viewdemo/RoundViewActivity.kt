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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round_view)

        handler = MyHandler(this)

        btnStartCountdown.setOnClickListener {
            roundView.setStartAndFinishAngle(-90, 270)
            startCountdown()
        }
    }

    private fun startCountdown() {
        if (!roundView.finished()) {
            roundView.countDown()
            Log.d(TAG, "sendEmptyMessageDelayed")
            handler.sendEmptyMessageDelayed(1, 20)
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
