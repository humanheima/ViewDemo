package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        tintIvColorWithStatus()


        testTintBG()

    }

    /**
     * 没有背景时，着色，不起作用。有背景，但是背景透明度不一样的时候，着色效果也是不一样的。
     */
    private fun testTintBG() {
        binding.ivNoBg.backgroundTintList = ColorStateList.valueOf(getColor(R.color.colorAccent))
        binding.ivHalfTransparentBg.backgroundTintList =
            ColorStateList.valueOf(getColor(R.color.colorAccent))
        binding.ivHalfNotTransparentBg.backgroundTintList =
            ColorStateList.valueOf(getColor(R.color.colorAccent))

    }

    private fun tintIvColor(iv: ImageView, resId: Int, color: Int) {
        //iv.imageTintList = ColorStateList.valueOf(color)
        iv.backgroundTintList = ColorStateList.valueOf(color)
    }

    /**
     * 选中状态下和非选中下，着色颜色不一样
     *
     */
    private fun tintIvColorWithStatus() {
        // 创建 ColorStateList
        val states = arrayOf(
            intArrayOf(android.R.attr.state_selected),  // 选中状态
            intArrayOf(-android.R.attr.state_selected) // 非选中状态
        )
        val tintColor = getColor(R.color.palette_neutral_90)
        val colors = intArrayOf(
            getColor(R.color.palette_blue_50),  // 选中状态：透明（不应用 tint）
            tintColor // 非选中状态：红色（可替换为你的颜色）
        )
        val colorStateList = ColorStateList(states, colors)

        binding.ivSelectedBg.imageTintList = colorStateList

        binding.ivSelectedBg.setOnClickListener {
            if (binding.ivSelectedBg.isSelected) {
                binding.ivSelectedBg.isSelected = false
            } else {
                binding.ivSelectedBg.isSelected = true
            }
        }
    }

}
