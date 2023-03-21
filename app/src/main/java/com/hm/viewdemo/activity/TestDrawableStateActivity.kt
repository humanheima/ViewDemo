package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2023/2/13
 * Desc: 测试多个View共同使用同一个Drawable的问题。
 */
class TestDrawableStateActivity : AppCompatActivity() {

    private lateinit var ivFirst: ImageView
    private lateinit var ivSecond: ImageView
    private lateinit var btnChangeSize: Button

    private lateinit var drawable: Drawable

    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, TestDrawableStateActivity::class.java)
            context.startActivity(starter)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_drawable_state)
        ivFirst = findViewById(R.id.ivFirst)
        ivSecond = findViewById(R.id.ivSecond)
        btnChangeSize = findViewById(R.id.btnChangeSize)

        btnChangeSize.setOnClickListener {
            val lp = ivFirst.layoutParams
            lp.height = 500
            ivFirst.requestLayout()
            //ivFirst.invalidate()
        }
        drawable = ColorDrawable(ContextCompat.getColor(this, R.color.colorAccent))
        setDrawable()
    }

    private fun setDrawable() {
        ivFirst.background = drawable
        ivSecond.background = drawable
    }

}