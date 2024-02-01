package com.example.soft_keyboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.soft_keyboard.funscreen.FullScreenSoftAdjustResizeActivity
import com.example.soft_keyboard.funscreen.FullScreenSoftAdjustpanActivity
import com.example.soft_keyboard.funscreen.FullScreenSoftFitSystemWindowsActivity
import com.example.soft_keyboard.scroll.ScrollAdjustPanActivity
import com.example.soft_keyboard.scroll.ScrollAdjustReSizeActivity

/**
 * Created by p_dmweidu on 2023/6/1
 * Desc: 测试软键盘相关的入口Activity
 */
class TestSoftKeyboardActivity : AppCompatActivity() {


    private lateinit var btnAdjustpan: Button
    private lateinit var btnAdjustResize: Button
    private lateinit var btnFitSystemWindows: Button
    private lateinit var et_test_send: EditText

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, TestSoftKeyboardActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_soft_keyboard)
        et_test_send = findViewById(R.id.et_test_send)
        et_test_send.setHorizontallyScrolling(false)
        et_test_send.maxLines = 5

        et_test_send.setOnEditorActionListener(object :
            android.widget.TextView.OnEditorActionListener {
            override fun onEditorAction(
                v: android.widget.TextView?,
                actionId: Int,
                event: android.view.KeyEvent?
            ): Boolean {
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
                    android.widget.Toast.makeText(
                        this@TestSoftKeyboardActivity,
                        "发送",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    return true
                }
                return false
            }
        })




        btnAdjustpan = findViewById(R.id.btn_adjustpan)
        btnAdjustResize = findViewById(R.id.btn_adjustresize)
        btnFitSystemWindows = findViewById(R.id.btn_FitSystemWindows)

    }

    public fun onClick(view: View) {
        when (view.id) {
            R.id.btn_adjustpan -> SoftAdjustpanActivity.launch(this)
            R.id.btn_adjustresize -> SoftAdjustResizeActivity.launch(this)
            R.id.btn_FitSystemWindows -> SoftFitSystemWindowsActivity.launch(this)
            R.id.btn_fullscreen_adjustpan -> FullScreenSoftAdjustpanActivity.launch(this)
            R.id.btn_fullscreen_adjustresize -> FullScreenSoftAdjustResizeActivity.launch(this)
            R.id.btn_fullscreen_FitSystemWindows -> FullScreenSoftFitSystemWindowsActivity.launch(
                this
            )

            R.id.btn_scroll_adjustpan -> ScrollAdjustPanActivity.launch(this)
            R.id.btn_scroll_adjustresize -> ScrollAdjustReSizeActivity.launch(this)
        }
    }


}