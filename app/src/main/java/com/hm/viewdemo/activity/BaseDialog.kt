package com.hm.viewdemo.activity

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.hm.viewdemo.R

/**
 * Created by dumingwei on 2019-10-21.
 * Desc:
 */
class BaseDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        val view = layoutInflater.inflate(R.layout.base_dialog_view, null, false)
        setContentView(view)
    }
}