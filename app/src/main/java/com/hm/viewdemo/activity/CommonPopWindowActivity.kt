package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.hm.viewdemo.R
import com.hm.viewdemo.util.ScreenUtil
import kotlinx.android.synthetic.main.activity_common_pop_window.*

/**
 * Created by dumingwei on 2020/11/13
 *
 * Desc:
 */
class CommonPopWindowActivity : AppCompatActivity() {

    private val TAG: String = "CommonPopWindowActivity"

    private lateinit var topPopWindow: PopupWindow
    private var topPopWindowWidth = 0
    private var topPopWindowHeight = 0


    private lateinit var bottomPopWindow: PopupWindow
    private var bottomPopWindowWidth = 0
    private var bottomPopWindowHeight = 0

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, CommonPopWindowActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_common_pop_window)
        initTopPopWindow()

        initBottomPopWindow()
        btnShowPopTop.setOnClickListener {
            if (!topPopWindow.isShowing) {

                val anchorPosition = IntArray(2)

                val screenWidth = ScreenUtil.getScreenWidth(this)

                btn_anchor.getLocationInWindow(anchorPosition)
                Log.i(TAG, "onCreate: height = ${anchorPosition[1]}")
                btn_anchor.getLocationOnScreen(anchorPosition)

                val widthAxis: Int = anchorPosition[0] + btn_anchor.measuredWidth / 2 - topPopWindowWidth / 2
                val heightAxis: Int = anchorPosition[1] - topPopWindowHeight

                topPopWindow.showAtLocation(btn_anchor, Gravity.TOP or Gravity.START, widthAxis, heightAxis)

                //使用showAsDropDown设置偏移量的方法让popwindow出现在控件上方
                //val anchorHeight = btn_anchor.measuredHeight
                //popWindow.showAsDropDown(btn_anchor, 0, -anchorHeight - popWindowHeight)
            }
        }

        btnShowPopBottom.setOnClickListener {
            if (!bottomPopWindow.isShowing) {

                val anchorPosition = IntArray(2)

                val screenWidth = ScreenUtil.getScreenWidth(this)

                btn_anchor.getLocationInWindow(anchorPosition)
                Log.i(TAG, "onCreate: height = ${anchorPosition[1]}")
                btn_anchor.getLocationOnScreen(anchorPosition)

                //val widthAxis: Int = anchorPosition[0] + btn_anchor.measuredWidth / 2 - bottomPopWindowWidth / 2
                val widthAxis: Int = bottomPopWindowWidth / 2 - btn_anchor.measuredWidth / 2
                Log.d(TAG, "onCreate: widthAxis = $widthAxis")
                val heightAxis: Int = anchorPosition[1] - bottomPopWindowHeight
                //使用showAsDropDown方法
                /**
                 * 要保证箭头在锚点的中间，要满足以下公式
                 *
                 * 1.1 锚点的左坐标+锚点的width/2 = popwindow的左坐标 + widthAxis + popwindow的width/2
                 * 1.2 锚点的左坐标 = popwindow的左坐标
                 * 1.3 widthAxis = popwindow的width/2 - 锚点的width/2
                 *
                 * 如果 popwindow的width > 锚点的width  widthAxis>0 ,应该向左移动 ，偏移量为 -widthAxis
                 *
                 * 如果 popwindow的width < 锚点的width  widthAxis<0 ,应该向右移动 ，偏移量为 -widthAxis
                 *
                 *
                 */
                bottomPopWindow.showAsDropDown(btn_anchor, -widthAxis, 0)
            }
        }
    }

    private fun initTopPopWindow() {
        val view = LayoutInflater.from(this).inflate(R.layout.pop_simple_style_top, null)
        //要测量一下哦，不然显示会有问题
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        topPopWindowWidth = view.measuredWidth
        topPopWindowHeight = view.measuredHeight
        Log.i(TAG, "initPopWindow: popWindowWidth = $topPopWindowWidth popWindowHeight = $topPopWindowHeight")
        topPopWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        topPopWindow.isFocusable = true
        topPopWindow.isOutsideTouchable = true
        topPopWindow.setBackgroundDrawable(ColorDrawable(0x00000000))
    }

    private fun initBottomPopWindow() {
        val view = LayoutInflater.from(this).inflate(R.layout.pop_simple_style_bottom, null)
        //要测量一下哦，不然显示会有问题
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        bottomPopWindowWidth = view.measuredWidth
        bottomPopWindowHeight = view.measuredHeight
        bottomPopWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        bottomPopWindow.isFocusable = true
        bottomPopWindow.isOutsideTouchable = true
        bottomPopWindow.setBackgroundDrawable(ColorDrawable(0x00000000))
    }

}