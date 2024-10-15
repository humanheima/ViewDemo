package com.hm.viewdemo.calendar

import android.content.Context
import android.content.Intent
import android.util.Log
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityCalendarTestBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Created by p_dmweidu on 2024/10/12
 * Desc: 测试日历相关的使用
 */
class CalendarTestActivity : BaseActivity<ActivityCalendarTestBinding>() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, CalendarTestActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun createViewBinding(): ActivityCalendarTestBinding {
        return ActivityCalendarTestBinding.inflate(layoutInflater)
    }

    override fun initData() {

        binding.btnCheckRange.setOnClickListener {
//            method1()
//            Log.d(TAG, "分割线")
//            method2()

            testYear()
        }
    }

    fun method1() {

        //20分钟的秒数
        val twentyMinutes = 20 * 60

        //01:00:00 ~ 06:59:59 ，差了 5 小时 59 分钟 59 秒。
        val delta0 = 6 * 3600 - 1
        val inRange0 = DateTimeHelper.isCurrentTimeInRange(1, 0, 0, delta0)
        Log.i(TAG, "isCurrentTimeInRange: $inRange0")

        //07:00:00 ~ 15:59:59 ，差了8 小时 59 分钟 59 秒。
        val delta1 = 9 * 3600 - 1
        val inRange1 = DateTimeHelper.isCurrentTimeInRange(7, 0, 0, delta1)

        Log.i(TAG, "isCurrentTimeInRange: $inRange1")

        //16:00:00 ~ 19:59:59 ，差了 3 小时 59 分钟 59 秒。
        val delta2 = 4 * 3600 - 1
        val inRange2 = DateTimeHelper.isCurrentTimeInRange(16, 0, 0, delta2)

        Log.i(TAG, "isCurrentTimeInRange: $inRange2")

        val delta3 = 5 * 3600 - 1
        val inRange3 = DateTimeHelper.isCurrentTimeInRange(20, 0, 0, delta3)

        Log.i(TAG, "isCurrentTimeInRange: $inRange3")

    }


    fun method2() {
        //20分钟的秒数
        val twentyMinutes = 20 * 60

        //01:00 ~ 01:20:00
        val inRange0 = DateTimeHelper.isCurrentTimeInRange(1, 0, 0, twentyMinutes)
        Log.i(TAG, "isCurrentTimeInRange: $inRange0")


        //07:00 ~ 07:20:00
        val inRange1 = DateTimeHelper.isCurrentTimeInRange(7, 0, 0, twentyMinutes)

        Log.i(TAG, "isCurrentTimeInRange: $inRange1")

        //14:00 ~ 14:20:00
        val inRange2 = DateTimeHelper.isCurrentTimeInRange(16, 0, 0, twentyMinutes)

        Log.i(TAG, "isCurrentTimeInRange: $inRange2")

        //20:00 ~ 20:20:00
        val inRange3 = DateTimeHelper.isCurrentTimeInRange(20, 0, 0, twentyMinutes)
        Log.i(TAG, "isCurrentTimeInRange: $inRange3")

    }

    private fun testYear() {
        // 设置到当前年份的最后一天
        val current = Calendar.getInstance().apply {
            set(Calendar.MONTH, Calendar.DECEMBER) // 12月
            set(Calendar.DAY_OF_MONTH, 31) // 31日
        }

        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        Log.i(TAG, "testYear: 当前时间  ${current.time}  ${df.format(current.time)}")

        // 增加一天
        current.set(Calendar.DAY_OF_YEAR, current.get(Calendar.DAY_OF_YEAR) + 1)

        Log.i(TAG, "testYear: 增加一天后的时间  ${current.time}  ${df.format(current.time)}")
    }


}