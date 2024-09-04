package com.hm.viewdemo.activity.design

import android.content.Context
import android.content.Intent
import android.graphics.Outline
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hm.viewdemo.databinding.ActivityCoordinateLayoutBinding
import com.hm.viewdemo.util.ScreenUtil
import com.hm.viewdemo.widget.MyNestedScrollView2

class CoordinateLayoutActivity : AppCompatActivity() {


    private val TAG = "CoordinateLayoutActivit"

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, CoordinateLayoutActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityCoordinateLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoordinateLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val lp = binding.ivBgMask.layoutParams as ViewGroup.MarginLayoutParams

        val initialTopMargin = lp.topMargin

        Log.i(TAG, "onCreate:  initialTopMargin = $initialTopMargin")

        binding.scrollView2.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Log.i(
                TAG,
                "onScrollChange:   scrollY = $scrollY oldScrollY = $oldScrollY"
            )

            lp.topMargin = initialTopMargin - scrollY
            binding.ivBgMask.layoutParams = lp

        }

        binding.llMaskContainer.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                view.clipToOutline = true
                outline.setRoundRect(
                    0,
                    0,
                    view.width,
                    view.height,
                    ScreenUtil.dpToPxFloat(this@CoordinateLayoutActivity, 23)
                )
            }
        }

        binding.scrollView2.setInterceptCallback(object : MyNestedScrollView2.InterceptCallback {
            override fun dontIntercept(): Boolean {
                val top = binding.tvTextContent.scrollY
                Log.i(
                    TAG,
                    "dontIntercept: binding.tvTextContent.scrollY = " + top
                )
                if (top > 0) {
                    return true
                }
                return false;
            }
        })
        Glide.with(this)
            //.load("https://xxvirtualcharactercdn.xxsypro.com/8B0C6E618460014119F1537BBBEFEA18.jpg")
            .load("https://imgservices-1252317822.image.myqcloud.com/coco/s11152023/bf3b4a97.jzoi4c.jpg")
            .into(binding.ivBg)
        binding.tvTextContent.movementMethod = ScrollingMovementMethod.getInstance()
    }

}
