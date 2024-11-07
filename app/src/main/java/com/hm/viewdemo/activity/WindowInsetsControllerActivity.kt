package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2024/11/7
 * Desc:测试 WindowInsetsController 的用法
 */
class WindowInsetsControllerActivity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, WindowInsetsControllerActivity::class.java)
            context.startActivity(starter)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window_insets_controller)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        hideSystemUI()
    }

    private fun hideSystemUI() {
        //让内容延伸到状态栏上
        WindowCompat.setDecorFitsSystemWindows(window, false)
        //注释1处，设置状态栏的背景颜色
        window.statusBarColor = Color.TRANSPARENT
        WindowCompat.getInsetsController(window, window.decorView).let { controller ->
            //controller.hide(WindowInsetsCompat.Type.systemBars())
//            controller.systemBarsBehavior =
//                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            //注释2处，false，状态栏上的颜色是白色的。true ，状态栏上的颜色是黑色的。
            controller.isAppearanceLightStatusBars = true
        }
    }


    @RequiresApi(Build.VERSION_CODES.R)
    fun initSystemBarsByAndroidX() {
        var controller = WindowCompat.getInsetsController(window, window.decorView)
        // 设置状态栏反色
        controller.isAppearanceLightStatusBars = true
        // 取消状态栏反色
        controller.isAppearanceLightStatusBars = false
        // 设置导航栏反色
        controller.isAppearanceLightNavigationBars = true
        // 取消导航栏反色
        controller.isAppearanceLightNavigationBars = false
        // 隐藏状态栏
        controller.hide(WindowInsets.Type.statusBars())
        // 显示状态栏
        controller.show(WindowInsets.Type.statusBars())
        // 隐藏导航栏
        controller.hide(WindowInsets.Type.navigationBars())
        // 显示导航栏
        controller.show(WindowInsets.Type.navigationBars())
        // 同时隐藏状态栏和导航栏
        controller.hide(WindowInsets.Type.systemBars())
        // 同时隐藏状态栏和导航栏
        controller.show(WindowInsets.Type.systemBars())
    }


}