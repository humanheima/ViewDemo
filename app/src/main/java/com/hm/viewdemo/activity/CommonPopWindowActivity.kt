package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

    private lateinit var popWindow: PopupWindow
    private var popWindowWidth = 0
    private var popWindowHeight = 0

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
        initPopWindow()
        btnShowPop1.setOnClickListener {
            if (!popWindow.isShowing) {

                val anchorPosition = IntArray(2)

                val screenWidth = ScreenUtil.getScreenWidth(this)

                btn_anchor.getLocationInWindow(anchorPosition)
                Log.i(TAG, "onCreate: height = ${anchorPosition[1]}")
                btn_anchor.getLocationOnScreen(anchorPosition)

                val widthAxis: Int = anchorPosition[0] + btn_anchor.measuredWidth / 2 - popWindowWidth / 2
                val heightAxis: Int = anchorPosition[1] - popWindowHeight

                popWindow.showAtLocation(btnShowPop1, Gravity.TOP or Gravity.START, widthAxis, heightAxis)

                //使用showAsDropDown设置偏移量的方法让popwindow出现在控件上方
                //val anchorHeight = btn_anchor.measuredHeight
                //popWindow.showAsDropDown(btn_anchor, 0, -anchorHeight - popWindowHeight)
            }
        }
    }

    private fun initPopWindow() {
        val view = LayoutInflater.from(this).inflate(R.layout.pop_simple_style_1, null)
        //要测量一下哦，不然显示会有问题
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        popWindowWidth = view.measuredWidth
        popWindowHeight = view.measuredHeight
        Log.i(TAG, "initPopWindow: popWindowWidth = $popWindowWidth popWindowHeight = $popWindowHeight")
        popWindow = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popWindow.isFocusable = true
        popWindow.isOutsideTouchable = true
        popWindow.setBackgroundDrawable(ColorDrawable(0x00000000))
    }

}