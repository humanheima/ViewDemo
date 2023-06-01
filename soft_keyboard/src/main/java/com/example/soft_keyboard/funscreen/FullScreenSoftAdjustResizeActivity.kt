package com.example.soft_keyboard.funscreen

import android.content.Context
import android.content.Intent
import com.example.soft_keyboard.BaseSoftKyeBoardAct
import com.example.soft_keyboard.R

/**
 * Created by p_dmweidu on 2023/6/1
 * Desc: 测试软键盘 AdjustResize 的 Activity
 */
class FullScreenSoftAdjustResizeActivity : BaseSoftKyeBoardAct() {

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, FullScreenSoftAdjustResizeActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_adjust_resize
    }
}