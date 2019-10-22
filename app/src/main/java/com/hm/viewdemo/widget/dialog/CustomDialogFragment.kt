package com.hm.viewdemo.widget.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-10-22.
 * Desc:
 */
class CustomDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_signin, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //点击阴影部分和返回键dialog是否消失
        //isCancelable = false

        dialog.window?.let {
            it.setGravity(Gravity.BOTTOM)
            //window.setWindowAnimations(R.style.animate_dialog)

            //设置背景透明，不然dialog周围会有padding
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }
}