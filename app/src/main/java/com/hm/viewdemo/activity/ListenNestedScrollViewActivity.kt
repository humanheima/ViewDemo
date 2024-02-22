package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hm.viewdemo.R
import com.hm.viewdemo.adapter.RecycleViewAdapter
import com.hm.viewdemo.base.BaseActivity
import com.hm.viewdemo.databinding.ActivityListenNestedScrollViewBinding
import com.hm.viewdemo.widget.NewNestedScrollView


/**
 * Created by p_dmweidu on 2023/2/2
 * Desc: 测试监听NestedScrollView滑动停止
 */
class ListenNestedScrollViewActivity : BaseActivity<ActivityListenNestedScrollViewBinding>() {

    private var dataList: MutableList<String> = arrayListOf()

    private var adapter: RecycleViewAdapter? = null
    private var layoutManager: LinearLayoutManager? = null

    private var recyclerView: RecyclerView? = null

    private var newNestedScrollView: NewNestedScrollView? = null

    private var iv_role_image: ImageView? = null

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, ListenNestedScrollViewActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun createViewBinding(): ActivityListenNestedScrollViewBinding {
        return ActivityListenNestedScrollViewBinding.inflate(layoutInflater)
    }

    override fun initData() {
        dataList = ArrayList()
        for (i in 0..20) {
            dataList.add("data$i")
        }
        iv_role_image = findViewById(R.id.iv_role_image)
        newNestedScrollView = findViewById(R.id.newNestedScrollView)
        recyclerView = findViewById(R.id.rv_gold_voice_list)
        layoutManager = LinearLayoutManager(this)
        adapter = RecycleViewAdapter(dataList, this)

        adapter?.setOnItemClickListener { view, position ->
            Toast.makeText(this, "item postion = ${position}", Toast.LENGTH_SHORT).show()
        }
        recyclerView?.setLayoutManager(layoutManager)
        recyclerView?.setAdapter(adapter)

        newNestedScrollView?.addScrollChangeListener(object :
            NewNestedScrollView.AddScrollChangeListener {

            override fun onScrollChange(
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {

            }

            override fun onScrollState(state: NewNestedScrollView.ScrollState) {
                when (state) {
                    NewNestedScrollView.ScrollState.DRAG -> {
                        //Log.i(TAG, "onScrollState: DRAG")
                    }

                    NewNestedScrollView.ScrollState.SCROLLING -> {
                        //Log.i(TAG, "onScrollState: SCROLLING")
                    }

                    NewNestedScrollView.ScrollState.IDLE -> {
                        val displayRect = Rect()

                        val b = iv_role_image?.getGlobalVisibleRect(displayRect) ?: false


                        Log.i(
                            TAG,
                            "onScrollState: IDLE b =$b displayRect = ${displayRect.height()} iv_role_image.height ${iv_role_image?.height}"
                        )
                        val viewLastGetGlobalVisibleRect =
                            iv_role_image?.getTag(R.string.view_last_get_global_visible_rect) as? Boolean?

                        if (b) {
                            if (viewLastGetGlobalVisibleRect == true) {

                                Log.i(
                                    TAG,
                                    "onScrollState: View 上次可见，不触发曝光 IDLE b =$b displayRect = ${displayRect.toString()} childCount = ${recyclerView?.childCount}"
                                )
                            } else {
                                Log.i(
                                    TAG,
                                    "onScrollState: View 上次不可见，需要曝光 IDLE b =$b displayRect = ${displayRect.toString()} childCount = ${recyclerView?.childCount} "
                                )
                            }
                            iv_role_image?.setTag(R.string.view_last_get_global_visible_rect, true)
                        } else {
                            iv_role_image?.setTag(R.string.view_last_get_global_visible_rect, false)
                        }

                        val layoutManager = recyclerView?.layoutManager
                        if (layoutManager is LinearLayoutManager) {
                            val firstPos = layoutManager.findFirstVisibleItemPosition()
                            val lastPos = layoutManager.findLastVisibleItemPosition()
                            val childCount = layoutManager.childCount
                            Log.i(
                                TAG,
                                "onScrollState: firstPos = $firstPos lastPos = $lastPos childCount = $childCount"
                            )
                            if (childCount > 0 && firstPos >= 0 && firstPos < childCount && firstPos <= lastPos && lastPos < childCount) {
                                for (index in firstPos until lastPos) {
                                    val childAt = layoutManager.getChildAt(index)
                                    val b = childAt?.getGlobalVisibleRect(displayRect) ?: false
                                    val viewLastGetGlobalVisibleRect =
                                        childAt?.getTag(R.string.view_last_get_global_visible_rect) as? Boolean?

                                    if (b) {
                                        if (viewLastGetGlobalVisibleRect == true) {

                                            Log.i(
                                                TAG,
                                                "onScrollState: View 上次可见，不触发曝光 IDLE b =$b displayRect = ${displayRect.toString()} position = $index childCount = ${childAt.hashCode()}"
                                            )
                                        } else {
                                            Log.i(
                                                TAG,
                                                "onScrollState: View 上次不可见，需要曝光 IDLE b =$b displayRect = ${displayRect.toString()} position = $index childCount = ${childAt.hashCode()} "
                                            )
                                        }
                                        childAt?.setTag(
                                            R.string.view_last_get_global_visible_rect,
                                            true
                                        )
                                    } else {
                                        childAt?.setTag(
                                            R.string.view_last_get_global_visible_rect,
                                            false
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }
        })

    }
}