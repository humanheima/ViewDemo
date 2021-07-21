package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_gradient_drawable.*

/**
 * Created by dumingwei on 2020/5/22
 *
 * Desc: GradientDrawable的使用
 *
 */
class GradientDrawableActivity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, GradientDrawableActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gradient_drawable)

        val background = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, intArrayOf(
                resources.getColor(R.color.vip_start_color),
                resources.getColor(R.color.vip_end_color
                )
        ))

        ivFirst.background = background
    }
}
