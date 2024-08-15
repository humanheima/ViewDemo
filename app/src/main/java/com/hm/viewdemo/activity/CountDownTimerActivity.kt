package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityCountDownTimerBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Crete by dumingwei on 2019-07-30
 * Desc: 测试CountDownTimer
 *
 */
class CountDownTimerActivity : BaseActivity<ActivityCountDownTimerBinding>() {

    var dataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, CountDownTimerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun createViewBinding(): ActivityCountDownTimerBinding {
        return ActivityCountDownTimerBinding.inflate(layoutInflater)
    }

    override fun initData() {
        binding.btnStart.setOnClickListener {
            startCountDown()
        }

        Log.d(TAG, "onCreate: SystemClock.elapsedRealtime() =" + SystemClock.elapsedRealtime())
        Log.d(
            TAG,
            "onCreate: SystemClock.elapsedRealtime() =" + dataFormat.format(SystemClock.elapsedRealtime())
        )

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
        binding.countdownView.initData(System.currentTimeMillis() + 1000 * 60)
        binding.countdownView.countDownFinishAction = {
            Toast.makeText(this, "倒计时结束", Toast.LENGTH_SHORT).show()
        }
        binding.countdownView.start()

    }

    override fun onStop() {
        super.onStop()
        binding.countdownView.cancel()
    }
}
