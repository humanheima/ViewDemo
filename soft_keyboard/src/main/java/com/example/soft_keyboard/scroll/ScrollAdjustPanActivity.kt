package com.example.soft_keyboard.scroll

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.soft_keyboard.R

/**
 * Created by p_dmweidu on 2023/6/1
 * Desc: 测试滚动布局 AdjustPan 的 Activity
 */
class ScrollAdjustPanActivity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, ScrollAdjustPanActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_adjust_pan)
    }
}