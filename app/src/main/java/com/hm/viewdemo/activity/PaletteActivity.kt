package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.Log
import androidx.core.graphics.ColorUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.BaseRvAdapter
import com.hm.viewdemo.adapter.BaseViewHolder
import kotlinx.android.synthetic.main.activity_palette.*

/**
 * Created by dumingwei on 2020/6/18
 *
 * Desc: 测试 Palete 和从图片中取色，并对取出的颜色做转换
 */
class PaletteActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, PaletteActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val list = arrayListOf<@androidx.annotation.ColorInt Int>()
    private val drawableList = arrayListOf<@DrawableRes Int>()

    private val TAG: String = "PaletteActivity"

    private var index = 0

    private val baseRvAdapter = object : BaseRvAdapter<Int>(this, list) {
        override fun bindViewHolder(holder: BaseViewHolder, color: Int, position: Int) {
            holder.setBackgroundColor(R.id.tvTitle, color)
            holder.setTextViewText(
                R.id.tvTitle,
                "第 $position 个颜色 , ${color.toString(16).toUpperCase()}"
            )
        }

        override fun getHolderType(position: Int, t: Int): Int {
            return R.layout.item_palette_color
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_palette)

        drawableList.add(R.drawable.balloon)
        drawableList.add(R.drawable.avatar)
        drawableList.add(R.drawable.ic_dog)

        rvColor.layoutManager = LinearLayoutManager(this)
        rvColor.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        rvColor.adapter = baseRvAdapter
        changeImage(R.drawable.balloon)

        btnChangeIv.setOnClickListener {
            var tempIndex = ++index
            tempIndex %= drawableList.size
            changeImage(drawableList[tempIndex])

        }
    }

    private fun changeImage(@DrawableRes resId: Int) {
        Glide.with(this).asBitmap().load(resId).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                Log.i(TAG, "onResourceReady: ")
                ivBitmap.setImageBitmap(resource)
                list.clear()
                Palette.from(resource).generate {

                    it?.swatches?.forEach { swatch ->
                        list.add(swatch.rgb)
                        val hsl = swatch.hsl
                        list.add(adapterC1Color(hsl))
                        list.add(adapterC2Color(hsl))
                        list.add(adapterC3Color(hsl))
                        list.add(swatch.bodyTextColor)
                        list.add(swatch.titleTextColor)
                    }

                    val color1 = it?.getDarkMutedColor(
                        ContextCompat.getColor(
                            this@PaletteActivity,
                            R.color.colorPrimary
                        )
                    ) ?: Color.RED
                    val color2 = it?.getDarkVibrantColor(
                        ContextCompat.getColor(
                            this@PaletteActivity,
                            R.color.colorPrimary
                        )
                    ) ?: Color.RED
                    val color3 = it?.getDominantColor(
                        ContextCompat.getColor(
                            this@PaletteActivity,
                            R.color.colorPrimary
                        )
                    ) ?: Color.RED
                    val color4 = it?.getLightMutedColor(
                        ContextCompat.getColor(
                            this@PaletteActivity,
                            R.color.colorPrimary
                        )
                    ) ?: Color.RED
                    val color5 = it?.getLightVibrantColor(
                        ContextCompat.getColor(
                            this@PaletteActivity,
                            R.color.colorPrimary
                        )
                    ) ?: Color.RED
                    val color6 = it?.getMutedColor(
                        ContextCompat.getColor(
                            this@PaletteActivity,
                            R.color.colorPrimary
                        )
                    ) ?: Color.RED
                    val color7 = it?.getVibrantColor(
                        ContextCompat.getColor(
                            this@PaletteActivity,
                            R.color.colorPrimary
                        )
                    ) ?: Color.RED



                    list.add(color1)
                    list.add(color2)
                    list.add(color3)
                    list.add(color4)
                    list.add(color5)
                    list.add(color6)
                    list.add(color7)

                    baseRvAdapter.notifyDataSetChanged()

                }
            }
        })
    }


    private fun adapterC1Color(hsl: FloatArray): Int {
        val s = hsl[1]
        val l = hsl[2]
        //H值不变
        //S最高20
        //L最高40
        hsl[1] = when {
            s >= 0.2f -> {
                0.2f
            }
            else -> {
                s
            }
        }
        hsl[2] = when {
            l >= 0.4f -> {
                0.4f
            }
            else -> {
                l
            }
        }
        return ColorUtils.HSLToColor(hsl)
    }

    private fun adapterC2Color(hsl: FloatArray): Int {
        hsl[1] = 0.4f
        val l = hsl[2]
        //H值不变
        //S固定40
        //L最低90
        hsl[2] = when {
            l <= 0.9f -> {
                0.9f
            }
            else -> {
                l
            }
        }
        return ColorUtils.HSLToColor(hsl)
    }

    private fun adapterC3Color(hsl: FloatArray): Int {
        val s = hsl[1]
        hsl[2] = 0.6f
        //H值不变
        //S最高12
        //L固定60
        hsl[1] = when {
            s >= 0.12f -> {
                0.12f
            }
            else -> {
                s
            }
        }
        return ColorUtils.HSLToColor(hsl)
    }
}
