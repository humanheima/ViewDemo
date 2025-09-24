package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityTestGetDrawingCachingBinding
import com.hm.viewdemo.util.SaveViewBitmapUtils

/**
 * Created by p_dmweidu on 2025/9/24
 * Desc: 测试获取ViewGroup上的 Bitmap
 */
class TestGetDrawingCachingActivity : BaseActivity<ActivityTestGetDrawingCachingBinding>() {

    //布局文件对应的view
    private var tvNumber: TextView? = null
    private var number = 0
    //用来显示生成的bitmap
    private var ivTop: ImageView? = null

    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, TestGetDrawingCachingActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun createViewBinding(): ActivityTestGetDrawingCachingBinding {
        return ActivityTestGetDrawingCachingBinding.inflate(layoutInflater)
    }

    override fun initData() {
        tvNumber = binding.includeLayoutDrawingCache.tvNumber
        binding.btnGetBitmap.setOnClickListener {
            number++
            //每次生成bitmap之前改变一下tvNumber的text
            tvNumber?.text = number.toString()
            // val bitmap = convertViewToBitmap2(tvNumber)
            val bitmap = SaveViewBitmapUtils.getViewBitmap(tvNumber)
            binding.ivTop.background = BitmapDrawable(resources, bitmap)
        }
    }

    /**
     * 通过drawingCache获取bitmap
     *
     * @param view
     * @return
     */
    private fun convertViewToBitmap2(view: View?): Bitmap {
        view!!.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        //如果不调用这个方法，每次生成的bitmap相同
        view.isDrawingCacheEnabled = false
        return bitmap
    }

    /**
     * 通过canvas复制view的bitmap
     *
     * @param view
     * @return
     */
    private fun copyByCanvas2(view: View): Bitmap {
        val width = view.measuredWidth
        val height = view.measuredHeight
        Log.i(TAG, "copyByCanvas: width=$width,height=$height")
        val bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bp)
        view.draw(canvas)
        canvas.save()
        return bp
    }

    /**
     * 通过drawingCache获取bitmap
     *
     * @param view
     * @return
     */
    private fun convertViewToBitmap(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        //Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        val bitmap = view.drawingCache
        //如果不调用这个方法，每次生成的bitmap相同
        view.isDrawingCacheEnabled = false
        return bitmap
    }

    /**
     * 通过canvas复制view的bitmap
     *
     * @param view
     * @return
     */
    private fun copyByCanvas(view: View): Bitmap {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val width = view.measuredWidth
        val height = view.measuredHeight
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        Log.i(TAG, "copyByCanvas: width=$width,height=$height")
        val bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bp)
        view.draw(canvas)
        canvas.save()
        return bp
    }

}
