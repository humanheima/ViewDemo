package com.hm.viewdemo.custom_view.chapter_10

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.hm.viewdemo.R
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityChapter10Binding
import com.hm.viewdemo.util.ImageUtil

class Chapter10Activity : BaseActivity<ActivityChapter10Binding>() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, Chapter10Activity::class.java)
            context.startActivity(intent)
        }
    }


    override fun createViewBinding(): ActivityChapter10Binding {
        return ActivityChapter10Binding.inflate(layoutInflater)
    }

    override fun initData() {

    }

    /**
     * 提取Alpha图像
     */
    private fun extractAlpha() {
        val srcBmp = BitmapFactory.decodeResource(resources, R.drawable.cat_dog)

        val bitmap = Bitmap.createBitmap(srcBmp.width, srcBmp.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.color = Color.CYAN
        canvas.drawBitmap(srcBmp.extractAlpha(), 0f, 0f, paint)

        binding.iv.setImageBitmap(bitmap)

        srcBmp.recycle()
    }

    /**
     * 发光效果
     */
    private fun advanceExtractAlpha() {
        val srcBmp = BitmapFactory.decodeResource(resources, R.drawable.cat_dog_alpha)

        val alphaPaint = Paint()
        val blurMaskFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.NORMAL)
        alphaPaint.maskFilter = blurMaskFilter

        val offsetXY = IntArray(2)

        val alphaBitmap = srcBmp.extractAlpha(alphaPaint, offsetXY)
        Log.i(TAG, "advanceExtractAlpha: ${offsetXY[0]},${offsetXY[1]}")

        val bitmap =
            Bitmap.createBitmap(alphaBitmap.width, alphaBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.color = Color.CYAN
        canvas.drawBitmap(alphaBitmap, 0f, 0f, paint)

        canvas.drawBitmap(srcBmp, -offsetXY[0].toFloat(), -offsetXY[1].toFloat(), null)

        binding.iv.setImageBitmap(bitmap)

        srcBmp.recycle()
    }

    private fun testWaterMark() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.cat_dog)
        val watermark = BitmapFactory.decodeResource(resources, R.drawable.watermark)
        val result = ImageUtil.createBitmap(bitmap, watermark)
        binding.iv.setImageBitmap(result)
    }

}
