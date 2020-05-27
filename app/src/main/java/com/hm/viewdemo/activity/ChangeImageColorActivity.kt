package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_change_image_color.*

class ChangeImageColorActivity : AppCompatActivity() {


    private val TAG: String? = "ChangeImageColorActivit"

    private val colorList = arrayListOf<Int>()

    private var svgIndex = 0

    private var pngIndex = 0

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ChangeImageColorActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_image_color)

        colorList.add(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        colorList.add(ContextCompat.getColor(this, R.color.colorAccent))
        colorList.add(ContextCompat.getColor(this, R.color.vip_start_color))
        colorList.add(ContextCompat.getColor(this, R.color.vip_end_color))
        colorList.add(Color.parseColor("#ff3344"))

        btnChangeSvgColor.setOnClickListener {

            if (svgIndex == colorList.size) {
                svgIndex = 0
            }
            val color = colorList[svgIndex]

            //tintIvColor(ivSvg, R.drawable.ic_android_black_24dp, color)
            ivSvg.setColorFilter(color)
            svgIndex++
        }

        btnChangePngColor.setOnClickListener {
            if (pngIndex == colorList.size) {
                pngIndex = 0
            }
            val color = colorList[pngIndex]

            //tintIvColor(ivPng, R.drawable.main_ic_back_white, color)
            ivPng.setColorFilter(color)
            pngIndex++

        }

        btnLaunchActivity.setOnClickListener {
            AutoSizingTextViewActivity.launch(this)

        }
    }

    private fun tintIvColor(iv: ImageView, resId: Int, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv.imageTintList = ColorStateList.valueOf(color)
        } else {
            val drawable = ContextCompat.getDrawable(this, resId)
            if (drawable != null) {
                val wrappedDrawable = DrawableCompat.wrap(drawable).mutate()
                DrawableCompat.setTint(wrappedDrawable, color)
                iv.setImageDrawable(wrappedDrawable)
            }
        }
    }
}
