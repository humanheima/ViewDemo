package com.hm.viewdemo.guide

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FirstTimeTooltipStrategy(
    context: Context,
    private val contentLayoutId: Int,
    private val containerView: ViewGroup, //谁来添加 contentLayoutId
    private val preferenceKey: String,
    private val position: Position = Position.BOTTOM
) : BaseGuideStrategy(context) {

    private val TAG = "FirstTimeTooltipStrateg"

    enum class Position { TOP, BOTTOM, LEFT, RIGHT }

    private val prefs by lazy {
        context.getSharedPreferences("guide_prefs", Context.MODE_PRIVATE)
    }

    override fun shouldShow(): Boolean {
        //默认展示
        val show = prefs.getBoolean(preferenceKey, true)
        Log.d(TAG, "shouldShow: show = $show")
        return show
    }

    override fun show(anchorView: View, guideItem: GuideItem, dismissCallback: () -> Unit) {
        val view = LayoutInflater.from(context).inflate(contentLayoutId, null)
        positionView(view, anchorView)
        setupDismissBehavior(view, dismissCallback)
        view.postDelayed(
            {
                dismissCallback.invoke()
                containerView?.removeView(view)
            },
            2000
        )

    }

    override fun dismiss() {
        afterShown()
        Log.d(TAG, "dismiss: ")
    }

    override fun afterShown() {
        prefs.edit().putBoolean(preferenceKey, false).apply()
    }

    private fun positionView(guideView: View, anchorView: View) {
        anchorView.post {
            val location = IntArray(2)
            anchorView.getLocationOnScreen(location)

            when (position) {
                Position.BOTTOM -> {
                    //guideView.x = location[0] + anchorView.width / 2f - guideView.width / 2f
                    //guideView.y = location[1] + anchorView.height + 10f
                    containerView.addView(guideView)
                    //afterShown()
                }

                Position.TOP -> {
//                    guideView.x = location[0] + anchorView.width / 2f - guideView.width / 2f
//                    guideView.y = location[1] - guideView.height - 10f

                    containerView.addView(guideView)
                    //afterShown()

                }
                // 其他位置实现...
                Position.LEFT -> {

                }

                Position.RIGHT -> {

                }
            }
        }
    }

    private fun setupDismissBehavior(view: View, dismissCallback: () -> Unit) {
        view.setOnClickListener { dismissCallback() }
    }
}