package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.CoordinatorLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.RecycleViewAdapter
import com.hm.viewdemo.fragment.FullSheetDialogFragment
import com.hm.viewdemo.util.ScreenUtil
import kotlinx.android.synthetic.main.activity_bottom_sheet.*
import java.util.*


/**
 * Crete by dumingwei on 2019-06-24
 * Desc:
 */

class BottomSheetActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName
    private lateinit var behavior: BottomSheetBehavior<*>

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, BottomSheetActivity::class.java)
            context.startActivity(starter)
        }

        /**
         * 当bottom sheet 的偏移量大于MAX_OFFSET的时候，设置最大透明度为 MAX_ALPHA
         */
        const val MAX_OFFSET = 0.5f

        /**
         * 最大的透明度为 255x0.6
         */
        const val MAX_ALPHA: Int = 255 * 3 / 5
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_sheet)
        behavior = BottomSheetBehavior.from(scroll_bottom_sheet)
        val layoutParams = scroll_bottom_sheet.layoutParams as CoordinatorLayout.LayoutParams

        val stringList = ArrayList<String>()
        for (i in 0 until 1) {
            stringList.add("string$i")
        }
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.layoutManager = LinearLayoutManager(this)
        val adapter = RecycleViewAdapter(stringList, this)
        recycler_view.adapter = adapter
        if (stringList.size > 4) {
            layoutParams.height = ScreenUtil.dpToPx(this, 400)
            behavior.peekHeight = ScreenUtil.dpToPx(this, 100)
        } else {
            layoutParams.height = CoordinatorLayout.LayoutParams.WRAP_CONTENT
            behavior.peekHeight = ScreenUtil.dpToPx(this, 100)
        }
        scroll_bottom_sheet.layoutParams = layoutParams
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.e(TAG, "onStateChanged$newState")
            }

            /**
             * @param onSlide  bottom sheet 新的偏移量。取值范围在[-1,1]之间。bottom sheet 向上滑动的时候
             * 偏移量会增加。bottom sheet 从收缩到展开，onSlide取值范围是[0,1]。bottom sheet从隐藏到收缩状态，
             * onSlide取值范围是[-1,0]。
             */
            override fun onSlide(bottomSheet: View, onSlide: Float) {
                Log.e(TAG, "onSlide$onSlide")
                /**
                 * 向上滑动onSlide增加,如果onSlide超过最大偏移量，则设置透明度为最大透明度 MAX_ALPHA，不再改变。
                 * 否则透明度的计算公式为： 最大透明度*（滑动偏移量和最大偏移量的比例）
                 */
                if (onSlide > MAX_OFFSET) {
                    coordinatorLayout.setBackgroundColor(Color.argb((MAX_ALPHA),
                            0, 0, 0))
                } else {
                    coordinatorLayout.setBackgroundColor(
                            Color.argb((MAX_ALPHA * (onSlide / MAX_OFFSET)).toInt(), 0, 0, 0))
                }
            }
        })
    }


    fun btnTestBottomSheet(view: View) {
        if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else if (behavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    fun btnTestBottomSheetDialog(view: View) {
        showBottomSheetDialog()

    }

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet, null)
        dialog.setContentView(view)
        dialog.show()
    }

    fun btnTestBottomSheetDialogFragment(view: View) {
        FullSheetDialogFragment().show(supportFragmentManager, "dialog")

    }

}
