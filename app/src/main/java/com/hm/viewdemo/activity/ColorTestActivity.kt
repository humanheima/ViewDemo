package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.FloatRange
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.BaseRvAdapter
import com.hm.viewdemo.adapter.BaseViewHolder
import kotlinx.android.synthetic.main.activity_color_test.*

/**
 * Created by dumingwei on 2020/7/7
 *
 * Desc: 测试 rgb，hsv
 *
 * 参考链接：
 * https://zhuanlan.zhihu.com/p/67930839
 * https://www.w3schools.com/colors/colors_hsl.asp
 *
 */
class ColorTestActivity : AppCompatActivity() {


    private val list = arrayListOf<FloatArray>()

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, ColorTestActivity::class.java)
            context.startActivity(intent)
        }
    }


    private val baseRvAdapter = object : BaseRvAdapter<FloatArray>(this, list) {
        override fun bindViewHolder(holder: BaseViewHolder, hsv: FloatArray, position: Int) {
            val hsvToColor = Color.HSVToColor(hsv)
            holder.setBackgroundColor(R.id.tvTitle, hsvToColor)
            holder.setTextViewText(R.id.tvTitle, "{${hsv[0]},${hsv[1]},${hsv[2]}}")
        }

        override fun getHolderType(position: Int, hsv: FloatArray): Int {
            return R.layout.item_palette_color
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_test)
        rvColor.layoutManager = LinearLayoutManager(this)
        rvColor.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        rvColor.adapter = baseRvAdapter
        changeHsv()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btnChangeHsv -> {
                changeHsv()
            }
            R.id.btnChangeHSL -> {
                changeHsl()
            }
            /* R.id.btnChangeSaturation -> {
                 changeSaturation(0f, 0.4f)
             }
             R.id.btnChangeValue -> {
                 changeValue(0f, 1f)
             }*/

        }
    }

    /**
     * 改变hsv
     */
    private fun changeHsv() {
        val hue = etHue.text.toString().toFloatOrNull()
        val saturation = etSaturation.text.toString().toFloatOrNull()
        val value = etValue.text.toString().toFloatOrNull()

        if (hue != null && saturation != null && value != null) {
            val hsv = FloatArray(3)
            hsv[0] = hue
            hsv[1] = saturation
            hsv[2] = value

            list.clear()
            list.add(hsv)
        }

        baseRvAdapter.notifyDataSetChanged()
    }

    /**
     * 改变hsl
     */
    private fun changeHsl() {
        val hue = etHue2.text.toString().toFloatOrNull()
        val saturation = etSaturation2.text.toString().toFloatOrNull()
        val light = etLight.text.toString().toFloatOrNull()

        if (hue != null && saturation != null && light != null) {
            val hsl = FloatArray(3)
            hsl[0] = hue
            hsl[1] = saturation
            hsl[2] = light

            list.clear()
            list.add(hsl)
        }

        baseRvAdapter.notifyDataSetChanged()
    }

    /**
     * 改变饱和度
     */
    private fun changeSaturation(@FloatRange(from = 0.0, to = 360.0) hue: Float,
                                 @FloatRange(from = 0.0, to = 1.0) value: Float) {
        list.clear()
        val step = 0.1f
        var saturation = 0f
        while (saturation < 1f) {
            val hsv = FloatArray(3)
            hsv[0] = hue
            hsv[1] = saturation
            hsv[2] = value
            saturation += step
            list.add(hsv)
        }
        baseRvAdapter.notifyDataSetChanged()
    }

    /**
     * 改变明度
     */
    private fun changeValue(@FloatRange(from = 0.0, to = 360.0) hue: Float,
                            @FloatRange(from = 0.0, to = 1.0) saturation: Float) {
        list.clear()
        val step = 0.1f
        var value = 0f
        while (value < 1f) {
            val hsv = FloatArray(3)
            hsv[0] = hue
            hsv[1] = saturation
            hsv[2] = value
            value += step
            list.add(hsv)
        }
        baseRvAdapter.notifyDataSetChanged()
    }

}
