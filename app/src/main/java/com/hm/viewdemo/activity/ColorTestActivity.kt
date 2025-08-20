package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.FloatRange
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.BaseRvAdapter
import com.hm.viewdemo.adapter.BaseViewHolder
import com.hm.viewdemo.databinding.ActivityColorTestBinding

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


    private val TAG = "ColorTestActivity"

    private val list = arrayListOf<FloatArray>()

    private lateinit var binding: ActivityColorTestBinding

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
            val text = "{${hsv[0]},${hsv[1]},${hsv[2]}} ， 对应16进制颜色为${
                hsvToHex(
                    hsv[0],
                    hsv[1],
                    hsv[2]
                )
            }"
            Log.d(TAG, "bindViewHolder: text = $text")
            holder.setTextViewText(
                R.id.tvTitle, text
            )
        }

        override fun getHolderType(position: Int, hsv: FloatArray): Int {
            return R.layout.item_palette_color
        }

        override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
            super.onViewRecycled(holder)
            Log.d(TAG, "onViewRecycled: ")
            holder.itemView.post {
                Log.d(TAG, "After delay onViewRecycled: ")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityColorTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvColor.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvColor.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                this,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
            )
        )
        binding.rvColor.adapter = baseRvAdapter
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
        val hue = binding.etHue.text.toString().toIntOrNull()
        val saturation = binding.etSaturation.text.toString().toFloatOrNull()
        val value = binding.etValue.text.toString().toFloatOrNull()

        if (hue != null && saturation != null && value != null) {
            val hsv = FloatArray(3)
            hsv[0] = hue.toFloat()
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
        val hue = binding.etHue2.text.toString().toIntOrNull()
        val saturation = binding.etSaturation2.text.toString().toFloatOrNull()
        val light = binding.etLight.text.toString().toFloatOrNull()

        if (hue != null && saturation != null && light != null) {
            val hsl = FloatArray(3)
            hsl[0] = hue.toFloat()
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
    private fun changeSaturation(
        @FloatRange(from = 0.0, to = 360.0) hue: Float,
        @FloatRange(from = 0.0, to = 1.0) value: Float
    ) {
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
    private fun changeValue(
        @FloatRange(from = 0.0, to = 360.0) hue: Float,
        @FloatRange(from = 0.0, to = 1.0) saturation: Float
    ) {
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

    /**
     * 将 HSV 颜色转换为 16 进制颜色字符串
     * @param hue 色相 (0-360)
     * @param saturation 饱和度 (0-1)
     * @param value 明度 (0-1)
     * @return 16 进制颜色字符串，例如 "#FF0000"
     */
    fun hsvToHex(hue: Float, saturation: Float, value: Float): String {
        // 规范化输入
        val h = hue % 360f // 确保色相在 0-360 范围内
        val s = saturation.coerceIn(0f, 1f) // 限制饱和度在 0-1
        val v = value.coerceIn(0f, 1f) // 限制明度在 0-1

        // HSV 转 RGB
        val c = v * s // 色度
        val x = c * (1 - Math.abs((h / 60f) % 2 - 1))
        val m = v - c

        var r = 0f
        var g = 0f
        var b = 0f

        when (h) {
            in 0f..60f -> {
                r = c
                g = x
                b = 0f
            }

            in 60f..120f -> {
                r = x
                g = c
                b = 0f
            }

            in 120f..180f -> {
                r = 0f
                g = c
                b = x
            }

            in 180f..240f -> {
                r = 0f
                g = x
                b = c
            }

            in 240f..300f -> {
                r = x
                g = 0f
                b = c
            }

            in 300f..360f -> {
                r = c
                g = 0f
                b = x
            }
        }

        // 转换为 0-255 的 RGB 值
        val red = ((r + m) * 255).toInt().coerceIn(0, 255)
        val green = ((g + m) * 255).toInt().coerceIn(0, 255)
        val blue = ((b + m) * 255).toInt().coerceIn(0, 255)

        // 格式化为 16 进制字符串
        return String.format("#%02X%02X%02X", red, green, blue)
    }

}
