package com.example.soft_keyboard.funscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.soft_keyboard.BaseSoftKyeBoardAct
import com.example.soft_keyboard.R


/**
 * Created by p_dmweidu on 2023/6/1
 * Desc: 测试软键盘Adjustpan的Activity
 */
class FullScreenSoftAdjustpanActivity : BaseSoftKyeBoardAct() {

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, FullScreenSoftAdjustpanActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_soft_adjustpan
    }

}