package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.widget.MyViewGroup

/**
 * Created by p_dmweidu on 2022/10/23
 * Desc: 测试View的绘制层级
 */
class MyViewGroupActivity : AppCompatActivity() {


    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, MyViewGroupActivity::class.java)
            context.startActivity(starter)
        }
    }

    lateinit var fm_root_layout: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_view_group)
        fm_root_layout = findViewById(R.id.fm_root_layout)
        //先把 MyViewGroup 添加到 Activity 的根布局文件中
        val viewGroup = MyViewGroup(this)

        fm_root_layout.addView(
            viewGroup,
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        val firstView = TextView(this).apply {
            text = "firstView"
            textSize = 20f
            gravity = Gravity.CENTER
            setTextColor(resources.getColor(R.color.white))
            background = ColorDrawable(resources.getColor(R.color.colorPrimary))
        }
        val secondView = TextView(this).apply {
            text = "secondView"
            textSize = 20f
            gravity = Gravity.CENTER
            setTextColor(resources.getColor(R.color.white))
            background = ColorDrawable(resources.getColor(R.color.colorAccent))
        }


        //依次添加 mFirstView 和 mSecondView
        viewGroup.mFirstView = firstView
        viewGroup.mSecondView = secondView

    }
}