package com.example.soft_keyboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by p_dmweidu on 2023/6/1
 * Desc: 测试软键盘 AdjustResize 的 Activity
 * 非全屏模式下，使用 AdjustResize，不需要额外处理。
 */
class SoftAdjustResizeActivity : AppCompatActivity() {

    private lateinit var rl_action_list: RelativeLayout
    private lateinit var rl_input_new_category_name: RelativeLayout

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, SoftAdjustResizeActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adjust_resize)

        rl_action_list = findViewById(R.id.rl_action_list)
        rl_input_new_category_name = findViewById(R.id.rl_input_new_category_name)

        findViewById<TextView>(R.id.tv_rename_category).setOnClickListener {
            rl_action_list.visibility = View.GONE
            rl_input_new_category_name.visibility = View.VISIBLE
        }

        findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            rl_action_list.visibility = View.VISIBLE
            rl_input_new_category_name.visibility = View.GONE
        }

    }
}