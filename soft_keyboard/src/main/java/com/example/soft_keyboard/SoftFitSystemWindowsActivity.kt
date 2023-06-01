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
 * Desc: 非全屏下，测试软键盘 fitSystemWindows 的 Activity，看起里不需要额外处理。
 */
class SoftFitSystemWindowsActivity : AppCompatActivity() {

    private lateinit var rl_action_list: RelativeLayout
    private lateinit var rl_input_new_category_name: RelativeLayout

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, SoftFitSystemWindowsActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fit_system_windows)

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