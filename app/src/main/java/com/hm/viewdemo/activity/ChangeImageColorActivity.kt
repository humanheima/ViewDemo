package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ActivityChangeImageColorBinding

class ChangeImageColorActivity : AppCompatActivity() {


    private val TAG: String? = "ChangeImageColorActivit"

    private val colorList = arrayListOf<Int>()

    private var svgIndex = 0

    private var pngIndex = 0

    private var llRoot: LinearLayout? = null

    private lateinit var binding: ActivityChangeImageColorBinding

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ChangeImageColorActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangeImageColorBinding.inflate(layoutInflater)

        setContentView(binding.root)
        llRoot = findViewById(R.id.ll_root)

        llRoot?.backgroundTintList = ColorStateList.valueOf(getColor(R.color.colorAccent))

        colorList.add(ContextCompat.getColor(this, R.color.white))
        colorList.add(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        colorList.add(ContextCompat.getColor(this, R.color.colorAccent))
        colorList.add(ContextCompat.getColor(this, R.color.vip_start_color))
        colorList.add(ContextCompat.getColor(this, R.color.vip_end_color))
        colorList.add(Color.parseColor("#ff3344"))

        //tintIvColor(ivPng, R.drawable.bookshelf_icon_edit, getColor(R.color.white))

        binding.btnChangeSvgColor.setOnClickListener {

            if (svgIndex == colorList.size) {
                svgIndex = 0
            }
            val color = colorList[svgIndex]

            tintIvColor(binding.ivSvg, R.drawable.ic_android_black_24dp, color)
            //ivSvg.setColorFilter(color)
            svgIndex++
        }

        binding.btnChangePngColor.setOnClickListener {
            if (pngIndex == colorList.size) {
                pngIndex = 0
            }
            val color = colorList[pngIndex]

            //tintIvColor(ivPng, R.drawable.main_ic_back_white, color)
            binding.ivPng.setColorFilter(color)
            pngIndex++

        }

        binding.btnLaunchActivity.setOnClickListener {
            AutoSizingTextViewActivity.launch(this)

        }
    }

    private fun tintIvColor(iv: ImageView, resId: Int, color: Int) {
        iv.imageTintList = ColorStateList.valueOf(color)
    }
}
