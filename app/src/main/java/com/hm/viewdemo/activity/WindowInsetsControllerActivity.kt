package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.WindowInsets
import androidx.annotation.RequiresApi
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityWindowInsetsControllerBinding

/**
 * Created by p_dmweidu on 2024/11/7
 * Desc:测试 WindowInsetsController 的用法
 */
class WindowInsetsControllerActivity : BaseActivity<ActivityWindowInsetsControllerBinding>() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, WindowInsetsControllerActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun createViewBinding(): ActivityWindowInsetsControllerBinding {

        return ActivityWindowInsetsControllerBinding.inflate(layoutInflater)
    }

    override fun initData() {
        changeStatusBarUI()
        binding.btnHideSystemBars.setOnClickListener {
            hide()
        }
        binding.btnShowSystemBars.setOnClickListener {
            show()
        }
    }

    private fun changeStatusBarUI() {
        //让内容延伸到状态栏上
        WindowCompat.setDecorFitsSystemWindows(window, false)
        //注释1处，设置状态栏的背景颜色
        window.statusBarColor = Color.TRANSPARENT
        WindowCompat.getInsetsController(window, window.decorView).let { controller ->
            //controller.hide(WindowInsetsCompat.Type.systemBars())
            //controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            //注释2处，false，状态栏上的颜色是白色的。true ，状态栏上的颜色是黑色的。
            //controller.isAppearanceLightStatusBars = true

        }
    }

    private fun hide() {
        // 获取 WindowInsetsController
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        // 隐藏状态栏
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
    }

    private fun show() {
        // 获取 WindowInsetsController
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        // 显示状态栏
        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun initSystemBarsByAndroidX() {
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