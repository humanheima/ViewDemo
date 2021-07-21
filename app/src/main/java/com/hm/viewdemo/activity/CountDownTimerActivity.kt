package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_count_down_timer.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Crete by dumingwei on 2019-07-30
 * Desc: 测试CountDownTimer
 *
 */
class CountDownTimerActivity : AppCompatActivity() {

    private val TAG = "CountDownTimerActivity"

    var dataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, CountDownTimerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down_timer)
        btnStart.setOnClickListener {
            startCountDown()
        }

        Log.d(TAG, "onCreate: SystemClock.elapsedRealtime() =" + SystemClock.elapsedRealtime())
        Log.d(TAG, "onCreate: SystemClock.elapsedRealtime() =" + dataFormat.format(SystemClock.elapsedRealtime()))

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        Log.i(TAG, "onCreate: ${dataFormat.format(Date(calendar.timeInMillis))}")
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        Log.i(TAG, "onCreate: ${dataFormat.format(Date(calendar.timeInMillis))}")
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        Log.i(TAG, "onCreate: ${dataFormat.format(Date(calendar.timeInMillis))}")
    }

    private fun startCountDown() {
        //countDownTimer.start()
        countdownView.initData(System.currentTimeMillis() + 1000 * 60)
        countdownView.countDownFinishAction = {
            Toast.makeText(this, "倒计时结束", Toast.LENGTH_SHORT).show()
        }
        countdownView.start()

    }

    override fun onStop() {
        super.onStop()
        countdownView.cancel()
    }
}
