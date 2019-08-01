package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
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

    var remainTime = 10L

    var dataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    companion object {

        const val HOUR = 3600
        const val MINUTE = 60

        fun launch(context: Context) {
            val intent = Intent(context, CountDownTimerActivity::class.java)
            context.startActivity(intent)
        }
    }

    lateinit var countDownTimer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count_down_timer)


        /* countDownTimer = object : CountDownTimer(remainTime * 1000, 1000L) {

             override fun onTick(millisUntilFinished: Long) {

                 val second = millisUntilFinished / 1000
                 val hour = second / HOUR
                 val minute = second % HOUR / MINUTE
                 val s = second % 60

                 var orderDec = ""

                 if (minute >= 0) {
                     orderDec = orderDec.plus(minute).plus("分")
                 }

                 if (s >= 0) {
                     orderDec = orderDec.plus(s).plus("秒")
                 }
                 tvTime.text = getString(R.string.hour_minute_second_format,
                         String.format("%02d", hour),
                         String.format("%02d", minute),
                         String.format("%02d", s))
                 //String.format("%2d", hour) + String.format("%2d", second) + String.format("%2d", s)
             }

             override fun onFinish() {
                 tvTime.text = "finish"
             }

         }*/

        btnStart.setOnClickListener {
            startCountDown()
        }

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        Log.d(TAG, "onCreate: ${dataFormat.format(Date(calendar.timeInMillis))}")
        calendar.add(Calendar.DAY_OF_YEAR,1)
        Log.d(TAG, "onCreate: ${dataFormat.format(Date(calendar.timeInMillis))}")
        calendar.add(Calendar.DAY_OF_YEAR,1)
        Log.d(TAG, "onCreate: ${dataFormat.format(Date(calendar.timeInMillis))}")
    }

    private fun startCountDown() {
        //countDownTimer.start()
        countdownView.initData(System.currentTimeMillis() + 1000 * 60)
        countdownView.countDownFinishAction = {
            Toast.makeText(this, "倒计时结束", Toast.LENGTH_SHORT).show()
        }
        countdownView.start()
    }
}
