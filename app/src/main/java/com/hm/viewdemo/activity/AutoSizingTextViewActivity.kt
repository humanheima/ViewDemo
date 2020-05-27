package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.widget.TextViewCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.widget.TextView
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_auto_sizing_text_view.*

/**
 * Created by dumingwei on 2020/5/26
 *
 * Desc: TextView和AppCompatTextView自动调整字体大小
 */
class AutoSizingTextViewActivity : AppCompatActivity() {

    val char = "自"

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, AutoSizingTextViewActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_sizing_text_view)

        //sdk版本大于等于26
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvDynamicSet.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
        } else {
            TextViewCompat.setAutoSizeTextTypeWithDefaults(tvDynamicSet,
                    TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
            )
        }*/


        //sdk版本大于等于26
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvDynamicSet.setAutoSizeTextTypeUniformWithConfiguration(16, 40, 1, TypedValue.COMPLEX_UNIT_SP
            )
        } else {
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                    tvDynamicSet, 16, 40, 1, TypedValue.COMPLEX_UNIT_SP
            )
        }*/

        //获取预设的字体大小数字
        val intArray = resources.getIntArray(R.array.autosize_text_sizes)
        //sdk版本大于等于26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tvDynamicSet.setAutoSizeTextTypeUniformWithPresetSizes(intArray, TypedValue.COMPLEX_UNIT_SP
            )
        } else {
            TextViewCompat.setAutoSizeTextTypeUniformWithPresetSizes(
                    tvDynamicSet, intArray, TypedValue.COMPLEX_UNIT_SP
            )
        }

        btnAdd.setOnClickListener {
            addText(tvDynamicSet)
            addText(acTvXml)
            addText(tvPreSet)
        }

        btnMinus.setOnClickListener {
            minusText(tvDynamicSet)
            minusText(acTvXml)
            minusText(tvPreSet)
        }
    }

    private fun addText(textView: TextView) {
        var text = textView.text as String
        text = text.plus(char)
        textView.text = text

    }

    private fun minusText(textView: TextView) {
        var text = textView.text as String
        val length = text.length
        if (length > 1) {
            text = text.substring(0, length - 1)
            textView.text = text
        }
    }

}
