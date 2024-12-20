package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.TextViewCompat
import com.hm.viewdemo.databinding.ActivityAutoSizingTextViewBinding

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

    private lateinit var binding: ActivityAutoSizingTextViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAutoSizingTextViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        //val intArray = resources.getIntArray(R.array.autosize_text_sizes)
        val intArray = intArrayOf(10, 12, 13, 15, 16, 20, 22, 24)
        //sdk版本大于等于26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            binding.tvDynamicSet.setAutoSizeTextTypeUniformWithPresetSizes(
                intArray, TypedValue.COMPLEX_UNIT_SP
            )
        } else {
            TextViewCompat.setAutoSizeTextTypeUniformWithPresetSizes(
                binding.tvDynamicSet, intArray, TypedValue.COMPLEX_UNIT_SP
            )
        }

        binding.btnAdd.setOnClickListener {
            addText(binding.tvDynamicSet)
            addText(binding.acTvXml)
            addText(binding.tvPreSet)
        }

        binding.btnMinus.setOnClickListener {
            minusText(binding.tvDynamicSet)
            minusText(binding.acTvXml)
            minusText(binding.tvPreSet)
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
