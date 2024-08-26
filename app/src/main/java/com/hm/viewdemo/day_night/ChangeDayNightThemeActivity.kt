package com.hm.viewdemo.day_night

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2023/12/27
 * Desc: 切换日夜间模式
 */
class ChangeDayNightThemeActivity : AppCompatActivity() {


    companion object {

        private const val TAG = "ChangeDayNightThemeActi"

        fun launch(context: Context) {
            val starter = Intent(context, ChangeDayNightThemeActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_change_day_night_theme)
//        Log.i(TAG, "onCreate: ")
        setContent {
            MaterialTheme {
                Surface {
                    Column {
                        Button(onClick = {
                            changeMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }) {
                            Text(text = "切换日间主题")
                        }

                        Button(onClick = {
                            changeMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }) {
                            Text(text = "切换夜间主题")
                        }
                        Button(onClick = {
                            changeMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        }) {
                            Text(text = "日夜间主题跟随系统")
                        }
                    }
                }
            }
        }
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_change_day -> {
                // 切换为日间模式
                changeMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            R.id.btn_change_night -> {
                // 切换到夜间主题
                changeMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            R.id.btn_change_follow -> {
                // 切换为跟随系统模式
                changeMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    private fun changeMode(@AppCompatDelegate.NightMode mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        DarkModeManager.start(ChangeDayNightThemeActivity@ this)
        recreate()
    }
}