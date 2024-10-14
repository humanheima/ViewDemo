package com.hm.viewdemo.calendar

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Created by p_dmweidu on 2024/10/14
 * Desc: 检查是否在某个时间范围内
 */
object DateTimeHelper {

    private val TAG = "DateTimeHelper"

    /**
     * 判断是否在某个时间范围内
     * @param delta 秒，end 到 start 的差值，单位秒，必须大于等于0
     */
    fun isCurrentTimeInRange(
        startHour: Int,
        startMinute: Int,
        startSecond: Int,
        delta: Int,
    ): Boolean {
        val current = Calendar.getInstance()

        // 创建开始时间
        val startCalendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, startHour)
            set(Calendar.MINUTE, startMinute)
            set(Calendar.SECOND, startSecond)
        }

        // 测试 跨月跨年的情况
//        val startCalendar = Calendar.getInstance().apply {
//            //set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR))
//            set(Calendar.MONTH, Calendar.DECEMBER) // 10月
//            set(Calendar.DAY_OF_MONTH, 31) // 31日
//            //set(Calendar.HOUR_OF_DAY, 23) // 设置为23:59:59
//            //set(Calendar.MINUTE, 59)
//            //set(Calendar.SECOND, 59)
//
//            set(Calendar.HOUR_OF_DAY, startHour)
//            set(Calendar.MINUTE, startMinute)
//            set(Calendar.SECOND, startSecond)
//        }

        val startTime = startCalendar.timeInMillis
        val endTime = startTime + delta * 1000
        val currentTime = current.timeInMillis


        // 创建结束时间
        val endCalendar = Calendar.getInstance().apply {
            timeInMillis = endTime
        }

        Log.d(
            TAG,
            "formatCalendar:  start=" + formatCalendar(startCalendar) + ",end=" + formatCalendar(
                endCalendar
            ) + ",current=" + formatCalendar(current)
        )

        return (current.after(startCalendar) || current.equals(startCalendar)) &&
                (current.before(endCalendar) || current.equals(endCalendar))

        //使用 timeInMillis 来检查
        //val currentTimeInMillis = current.timeInMillis
        //return currentTimeInMillis in startCalendar.timeInMillis..endCalendar.timeInMillis
    }


    // 判断时间区间的函数
    fun isTimeInRange(current: Calendar, start: Calendar, end: Calendar): Boolean {
        return (current.after(start) || current.equals(start)) &&
                (current.before(end) || current.equals(end))
    }

    // 格式化 Calendar 的时间
    fun formatCalendar(calendar: Calendar): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd:HH:mm:ss", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

}