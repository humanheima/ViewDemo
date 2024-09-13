package com.hm.viewdemo.activity.design

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.BaseRvAdapter
import com.hm.viewdemo.adapter.BaseViewHolder
import com.hm.viewdemo.databinding.ActivityPinScrollTextBinding
import com.hm.viewdemo.util.ScreenUtil
import com.hm.viewdemo.widget.MyNestedScrollView2
import com.hm.viewdemo.widget.RoundedCornersTransformation

/**
 * Created by p_dmweidu on 2024/9/11
 * Desc: 吸顶 + 文字区域滑动 实现
 * 1. NestedScrollView 滑动到最大距离以后，继续向上滑，把吸顶区域 View 摘出来。
 * 2. 向下滑的时候，等于最大距离的时候， 再把吸顶区域 View add进来
 */
class PinScrollTextActivity : AppCompatActivity() {

    private val TAG = "CoordinateLayoutActivit"

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, PinScrollTextActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityPinScrollTextBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinScrollTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.simpleMaskViewGroup.setGradientEnabled(false)
        binding.rvTags.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvTags.adapter = baseRvAdapter

        val lp = binding.ivBgMask.layoutParams as ViewGroup.MarginLayoutParams

        val initialTopMargin = lp.topMargin

        Log.i(TAG, "onCreate:  initialTopMargin = $initialTopMargin")

        var maxScrollY = 0

        var maskHeight = 0
        binding.scrollView2.setOnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
            Log.i(
                TAG,
                "onScrollChange:   scrollY = $scrollY oldScrollY = $oldScrollY"
            )

            if (scrollY < maxScrollY) {
                lp.topMargin = initialTopMargin - scrollY
                binding.ivBgMask.layoutParams = lp
            }
            if (scrollY >= maxScrollY) {
                //这个时候，，透明区域要逐渐变化
                binding.simpleMaskViewGroup.setTransparentSize(scrollY - maxScrollY)
                binding.simpleMaskViewGroup.setGradientEnabled(true)

                //把吸顶布局摘出来
                Log.d(
                    TAG,
                    "onCreate: scrollY>= maxScrollY scrollY = $scrollY maxScrollY = $maxScrollY"
                )
                if (binding.flPinContainer.childCount == 0) {
                    binding.flFloatContainer.removeView(binding.rlDynamicContent)
                    val lp = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val dp16 = ScreenUtil.dpToPx(this, 16)
                    lp.leftMargin = dp16
                    lp.rightMargin = dp16
                    binding.flPinContainer.addView(binding.rlDynamicContent, lp)
                }

            } else {
                //把吸顶布局添加回来
                binding.simpleMaskViewGroup.setGradientEnabled(false)
                if (binding.flPinContainer.childCount == 1) {
                    binding.flPinContainer.removeView(binding.rlDynamicContent)
                    val lp = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val dp16 = ScreenUtil.dpToPx(this, 16)
                    lp.leftMargin = dp16
                    lp.rightMargin = dp16
                    binding.flFloatContainer.addView(binding.rlDynamicContent, lp)
                }
            }
        }
        binding.scrollView2.post {
            val selfHeight = binding.scrollView2.height
            val scrollContentHeight = binding.llScrollContent.height


            //maxScrollY = scrollContentHeight - selfHeight
            maxScrollY = ScreenUtil.dpToPx(this, 198)
            if (maxScrollY < 0) {
                maxScrollY = 0
            }
            maskHeight = binding.llMaskContainer.height

            Log.d(
                TAG,
                "onCreate: selfHeight = $selfHeight scrollContentHeight = $scrollContentHeight maxScrollY = $maxScrollY maskHeight = $maskHeight"
            )

            if (maxScrollY >= maskHeight) {
                maxScrollY = maskHeight
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
        //binding.tvTextContent.movementMethod = ScrollingMovementMethod.getInstance()
    }

    val list = mutableListOf<String>().apply {

        repeat(20) {
            add("Tag $it")
        }
    }

    private val baseRvAdapter = object : BaseRvAdapter<String>(this, list) {

        override fun getHolderType(position: Int, t: String): Int {
            return R.layout.item_tag
        }

        override fun bindViewHolder(holder: BaseViewHolder?, t: String, position: Int) {

            holder?.setTextViewText(R.id.tv_tag, t)

        }
    }

}
