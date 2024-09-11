package com.hm.viewdemo.activity.design

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.hm.viewdemo.databinding.ActivityCoordinateLayoutBinding
import com.hm.viewdemo.util.ScreenUtil
import com.hm.viewdemo.widget.MyNestedScrollView2
import com.hm.viewdemo.widget.RoundedCornersTransformation

/**
 * Created by p_dmweidu on 2024/9/11
 * Desc:
 * 1. 两个NestedScrollView，其中一个嵌套在另一个上，外层滑动到最大距离以后，继续向上滑，把吸顶区域 View 摘出来。
 * 2. 向下滑的时候，等于最大距离的时候， 再把吸顶区域 View add进来
 */
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
        binding.scrollView2.post {
            val selfHeight = binding.scrollView2.height
            val scrollContentHeight = binding.llScrollContent.height
            Log.d(
                TAG,
                "onCreate: selfHeight = $selfHeight scrollContentHeight = $scrollContentHeight"
            )

            var maxScrollY = scrollContentHeight - selfHeight
            if (maxScrollY < 0) {
                maxScrollY = 0
            }

            binding.scrollView2.setMaxScrollY(maxScrollY)


        }

        binding.llMaskContainer.radius = ScreenUtil.dpToPxFloat(this, 23)

        binding.scrollView2.setInterceptCallback(object : MyNestedScrollView2.InterceptCallback {
            override fun dontIntercept(): Boolean {
                val top = binding.tvTextContent.scrollY
                Log.i(
                    TAG,
                    "dontIntercept: binding.tvTextContent.scrollY = $top"
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
            .transform(RoundedCornersTransformation(ScreenUtil.dpToPx(this, 23), 0))
            .into(binding.ivBg)
        binding.tvTextContent.movementMethod = ScrollingMovementMethod.getInstance()
    }

}
