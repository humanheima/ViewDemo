package com.hm.viewdemo.widget.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-10-22.
 * Desc:
 */
class CustomDialogFragment() : DialogFragment() {

    private val TAG: String = "CustomDialogFragment"

    var webView: WebView? = null

    init {
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialogStyle1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(TAG, "onCreateView: ")
        val inflate = inflater.inflate(R.layout.dialog_web_view, container, false)

        val fl_webview_container = inflate.findViewById<FrameLayout>(R.id.fl_webview_container)
        webView?.let {
            val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            fl_webview_container.addView(it, layoutParams)
        }
        return inflate
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(TAG, "onActivityCreated: ")
        //点击阴影部分和返回键dialog是否消失
        //isCancelable = false

        dialog.window?.let {
            it.setGravity(Gravity.BOTTOM)
            //window.setWindowAnimations(R.style.animate_dialog)

            //设置背景透明，不然dialog周围会有padding
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }
}