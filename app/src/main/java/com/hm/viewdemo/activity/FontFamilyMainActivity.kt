package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ActivityFontFamilyMainBinding

/**
 * Created by dumingwei on 2021/1/13
 *
 * Desc: 测试TextView字体相关
 */
class FontFamilyMainActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, FontFamilyMainActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val text = "豫章故郡，洪都新府"
    private val list = arrayListOf<String>()

    private lateinit var binding: ActivityFontFamilyMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_font_family_main)

        list.add("sans-serif")
        //list.add("sans-serif-normal")
        //list.add("sans-serif-italic")

        list.add("sans-serif-thin")
        list.add("sans-serif-light")
        list.add("sans-serif-medium")
        list.add("sans-serif-black")
        list.add("sans-serif-condensed")
        list.add("serif")
        list.add("serif-bold")
        list.add("monospace")

        list.forEach {
            val textViewNormal = TextView(this)
            textViewNormal.paint.typeface = Typeface.create(it, Typeface.NORMAL)
            textViewNormal.text = text
            binding.llContainer.addView(textViewNormal)

            val textViewItalic = TextView(this)
            textViewItalic.paint.typeface = Typeface.create(it, Typeface.ITALIC)
            textViewItalic.text = text
            binding.llContainer.addView(textViewItalic)

        }
    }
}