package com.example.soft_keyboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.soft_keyboard.funscreen.FullScreenSoftAdjustResizeActivity
import com.example.soft_keyboard.funscreen.FullScreenSoftAdjustpanActivity
import com.example.soft_keyboard.funscreen.FullScreenSoftFitSystemWindowsActivity

/**
 * Created by p_dmweidu on 2023/6/1
 * Desc: 测试软键盘相关的入口Activity
 */
class TestSoftKeyboardActivity : AppCompatActivity() {


    private lateinit var btnAdjustpan: Button
    private lateinit var btnAdjustResize: Button
    private lateinit var btnFitSystemWindows: Button

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, TestSoftKeyboardActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_soft_keyboard)
        btnAdjustpan = findViewById(R.id.btn_adjustpan)
        btnAdjustResize = findViewById(R.id.btn_adjustresize)
        btnFitSystemWindows = findViewById(R.id.btn_FitSystemWindows)

    }

    public fun onClick(view: View){
        when(view.id){
            R.id.btn_adjustpan -> SoftAdjustpanActivity.launch(this)
            R.id.btn_adjustresize -> SoftAdjustResizeActivity.launch(this)
            R.id.btn_FitSystemWindows -> SoftFitSystemWindowsActivity.launch(this)
            R.id.btn_fullscreen_adjustpan -> FullScreenSoftAdjustpanActivity.launch(this)
            R.id.btn_fullscreen_adjustresize -> FullScreenSoftAdjustResizeActivity.launch(this)
            R.id.btn_fullscreen_FitSystemWindows -> FullScreenSoftFitSystemWindowsActivity.launch(this)
        }
    }


}