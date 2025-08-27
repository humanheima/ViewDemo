package com.hm.viewdemo.custom_view.chapter7

import android.content.Context
import android.content.Intent
import android.graphics.BlurMaskFilter
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.MaskFilterSpan
import android.util.Log
import android.widget.SeekBar
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityBlurMaskFilterBinding

/**
 * Created by p_dmweidu on 2025/8/27
 * Desc: 测试 BlurMaskFilter
 */
class BlurMaskFilterActivity : BaseActivity<ActivityBlurMaskFilterBinding>() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, BlurMaskFilterActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var blurRadius = 1

    override fun createViewBinding(): ActivityBlurMaskFilterBinding {
        return ActivityBlurMaskFilterBinding.inflate(layoutInflater)
    }

    override fun initData() {
        testBlurTextView()
    }

    private fun testBlurTextView() {
        binding.radiusSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.d(TAG, "onProgressChanged: p1 = $p1")
                //7 - 10 的效果比较好
                blurRadius = p1
                setBlurText()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        setBlurText()
    }

    private fun setBlurText() {
        val builder =
            SpannableStringBuilder("春花秋月何时了往事知多少小楼昨夜又东风故国不堪回首月明中。雕栏玉砌应犹在，只是朱颜改。问君能有几多愁？恰似一江春水向东流")



        if (blurRadius > 0) {

            //        val outBlurMaskFilterSpan =
//            MaskFilterSpan(BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.OUTER))
//        builder.setSpan(outBlurMaskFilterSpan, 0, 3, Spannable.SPAN_INCLUSIVE_INCLUSIVE)


            val normalBlurMaskFilterSpan =
                MaskFilterSpan(BlurMaskFilter(blurRadius.toFloat(), BlurMaskFilter.Blur.NORMAL))
            builder.setSpan(normalBlurMaskFilterSpan, 4, 17, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

            //        val innerBlurMaskFilterSpan =
//            MaskFilterSpan(BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.INNER))
//        builder.setSpan(innerBlurMaskFilterSpan, 8, 11, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

//        val solidBlurMaskFilterSpan =
//            MaskFilterSpan(BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.SOLID))
//        builder.setSpan(solidBlurMaskFilterSpan, 12, 15, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        }

        binding.textView10.text = builder
    }


}
