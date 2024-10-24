package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.BaseRvAdapter
import com.hm.viewdemo.adapter.BaseViewHolder
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityPaletteBinding

/**
 * Created by dumingwei on 2020/6/18
 *
 * Desc: 测试 Palete 和从图片中取色，并对取出的颜色做转换
 */
class PaletteActivity : BaseActivity<ActivityPaletteBinding>() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, PaletteActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val list = arrayListOf<Int>()
    private val drawableList = arrayListOf<Int>()


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

    override fun createViewBinding(): ActivityPaletteBinding {
        return ActivityPaletteBinding.inflate(layoutInflater)
    }

    override fun initData() {
        drawableList.add(R.drawable.test_palette)
        drawableList.add(R.drawable.test_palette2)
        drawableList.add(R.drawable.balloon)
        drawableList.add(R.drawable.ic_soft_avatar)
        drawableList.add(R.drawable.ic_dog)

        binding.rvColor.layoutManager = LinearLayoutManager(this)
        binding.rvColor.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        binding.rvColor.adapter = baseRvAdapter
        changeImage(R.drawable.test_palette)

        binding.btnChangeIv.setOnClickListener {
            var tempIndex = ++index
            tempIndex %= drawableList.size
            changeImage(drawableList[tempIndex])

        }
    }

    private fun changeImage(@DrawableRes resId: Int) {
        Glide.with(this).asBitmap().load(resId).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                Log.i(TAG, "onResourceReady: ")
                binding.ivBitmap.setImageBitmap(resource)
                list.clear()
                Palette.from(resource).generate {

                    it?.mutedSwatch?.let { swatch ->
                        val color = convertToBgColor(swatch.hsl)
                        list.add(color)
                    }
                    it?.darkMutedSwatch?.let { swatch ->
                        val color = convertToBgColor(swatch.hsl)
                        list.add(color)
                    }

                    it?.lightMutedSwatch?.let { swatch ->
                        val color = convertToBgColor(swatch.hsl)
                        list.add(color)
                    }

                    it?.vibrantSwatch?.let { swatch ->
                        val color = convertToBgColor(swatch.hsl)
                        list.add(color)
                    }

                    it?.darkVibrantSwatch?.let { swatch ->
                        val color = convertToBgColor(swatch.hsl)
                        list.add(color)
                    }

                    it?.lightVibrantSwatch?.let { swatch ->
                        val color = convertToBgColor(swatch.hsl)
                        list.add(color)
                    }

                    it?.dominantSwatch?.let { swatch ->
                        val color = convertToBgColor(swatch.hsl)
                        list.add(color)
                    }


//                    it?.swatches?.forEach { swatch ->
//                        list.add(swatch.rgb)
//                        val hsl = swatch.hsl
//                        list.add(adapterC1Color(hsl))
//                        list.add(adapterC2Color(hsl))
//                        list.add(adapterC3Color(hsl))
//                        list.add(swatch.bodyTextColor)
//                        list.add(swatch.titleTextColor)
//                    }
//
//                    val color1 = it?.getDarkMutedColor(
//                        ContextCompat.getColor(
//                            this@PaletteActivity,
//                            R.color.colorPrimary
//                        )
//                    ) ?: Color.RED
//                    val color2 = it?.getDarkVibrantColor(
//                        ContextCompat.getColor(
//                            this@PaletteActivity,
//                            R.color.colorPrimary
//                        )
//                    ) ?: Color.RED
//                    val color3 = it?.getDominantColor(
//                        ContextCompat.getColor(
//                            this@PaletteActivity,
//                            R.color.colorPrimary
//                        )
//                    ) ?: Color.RED
//                    val color4 = it?.getLightMutedColor(
//                        ContextCompat.getColor(
//                            this@PaletteActivity,
//                            R.color.colorPrimary
//                        )
//                    ) ?: Color.RED
//                    val color5 = it?.getLightVibrantColor(
//                        ContextCompat.getColor(
//                            this@PaletteActivity,
//                            R.color.colorPrimary
//                        )
//                    ) ?: Color.RED
//                    val color6 = it?.getMutedColor(
//                        ContextCompat.getColor(
//                            this@PaletteActivity,
//                            R.color.colorPrimary
//                        )
//                    ) ?: Color.RED
//                    val color7 = it?.getVibrantColor(
//                        ContextCompat.getColor(
//                            this@PaletteActivity,
//                            R.color.colorPrimary
//                        )
//                    ) ?: Color.RED
//
//
//
//                    list.add(color1)
//                    list.add(color2)
//                    list.add(color3)
//                    list.add(color4)
//                    list.add(color5)
//                    list.add(color6)
//                    list.add(color7)

                    baseRvAdapter.notifyDataSetChanged()

                }
            }
        })
    }


    /**
     * 转换的颜色做背景色使用
     *
     * @param hsl
     * @return
     */
    private fun convertToBgColor(hsl: FloatArray): Int {
        val h = hsl[0]
        var s = hsl[1]
        var l = hsl[2]
        Log.i(TAG, "convertToBgColor: h = $h, s = $s, l = $l")
        s = 0.52f
        l = 0.32f
        return ColorUtils.HSLToColor(floatArrayOf(h, s, l))
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
