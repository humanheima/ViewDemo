package com.hm.viewdemo.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.hm.viewdemo.alarm.YourBroadcastReceiver
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityAlarmManagerBinding


/**
 * Created by p_dmweidu on 2024/10/12
 * Desc: 测试使用 AlarmManager 执行定时任务
 */
class AlarmManagerActivity : BaseActivity<ActivityAlarmManagerBinding>() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, AlarmManagerActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun createViewBinding(): ActivityAlarmManagerBinding {
        return ActivityAlarmManagerBinding.inflate(layoutInflater)
    }

    val requestCode = 10086

    override fun initData() {
        binding.btnStart.setOnClickListener() {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(this, YourBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
            //val triggerTime = System.currentTimeMillis() + 4 * 60 * 60 * 1000 // 4小时后
            val triggerTime = System.currentTimeMillis() + 10
            alarmManager.set(AlarmManager.RTC, triggerTime, pendingIntent)
            startService(intent)
        }

        binding.btnCancel.setOnClickListener {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, YourBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)

        }
    }


}