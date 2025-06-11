package com.hm.viewdemo.guide

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager

class FullScreenGuideStrategy(
    context: Context,
    private val contentLayoutId: Int,
    private val highlightViewId: Int? = null
) : BaseGuideStrategy(context) {

    private var guideView: View? = null
    private var windowManager: WindowManager? = null

    override fun shouldShow(): Boolean {
        return true
    }


    override fun show(anchorView: View, guideItem: GuideItem, dismissCallback: () -> Unit) {
        val view = LayoutInflater.from(context).inflate(contentLayoutId, null)
        this.guideView = view
        this.windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

//        val layoutParams = createLayoutParams()
//        windowManager?.addView(view, layoutParams)

        setupHighlight(view, anchorView)
        //setupDismissBehavior(view, dismissCallback)
    }

    override fun dismiss() {
    }

    override fun afterShown() {
    }

    private fun setupHighlight(guideView: View, anchorView: View) {
        highlightViewId?.let { id ->
            val highlightView = guideView.findViewById<View>(id)
            anchorView.post {
                val location = IntArray(2)
                anchorView.getLocationOnScreen(location)

                highlightView.x = location[0].toFloat()
                highlightView.y = location[1].toFloat()
                highlightView.layoutParams.width = anchorView.width
                highlightView.layoutParams.height = anchorView.height
                highlightView.requestLayout()
            }
        }
    }


}