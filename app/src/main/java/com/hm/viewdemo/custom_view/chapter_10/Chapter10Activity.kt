package com.hm.viewdemo.custom_view.chapter_10

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ImageUtil
import kotlinx.android.synthetic.main.activity_chapter10.*

class Chapter10Activity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, Chapter10Activity::class.java)
            context.startActivity(intent)
        }
    }

    private val TAG = "Chapter10Activity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter10)

        //extractAlpha()
        //advanceExtractAlpha()
        //testWaterMark()

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

        iv.setImageBitmap(bitmap)

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
        Log.d(TAG, "advanceExtractAlpha: ${offsetXY[0]},${offsetXY[1]}")

        val bitmap = Bitmap.createBitmap(alphaBitmap.width, alphaBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.color = Color.CYAN
        canvas.drawBitmap(alphaBitmap, 0f, 0f, paint)

        canvas.drawBitmap(srcBmp, -offsetXY[0].toFloat(), -offsetXY[1].toFloat(), null)

        iv.setImageBitmap(bitmap)

        srcBmp.recycle()
    }

    private fun testWaterMark() {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.cat_dog)
        val watermark = BitmapFactory.decodeResource(resources, R.drawable.watermark)
        val result = ImageUtil.createBitmap(bitmap, watermark)
        iv.setImageBitmap(result)
    }

}
