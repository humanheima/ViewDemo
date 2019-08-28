package com.hm.viewdemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.activity_pop_window.*

class PopWindowActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val intent = Intent(context, PopWindowActivity::class.java)
            context.startActivity(intent)
        }
    }

    private var popupStyle1: PopupWindow? = null
    private var popupStyle2: PopupWindow? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_window)
    }

    fun onClick(view: View) {

        when (view.id) {
            R.id.btnStyle1 -> {
                showPopWindow1()
            }
            R.id.btnStyle2 -> {
                showPopWindow2()
            }
        }
    }

    private fun showPopWindow1() {
        if (popupStyle1 == null) {
            val contentView = LayoutInflater.from(this).inflate(R.layout.pop_content_view_style_1, null)
            popupStyle1 = PopupWindow(
                    contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    true
            )
        }
        popupStyle1?.showAtLocation(btnStyle1, Gravity.BOTTOM, 0, 0)

    }

    private fun showPopWindow2() {
        if (popupStyle2 == null) {
            val contentView = LayoutInflater.from(this).inflate(R.layout.pop_content_view_style_2, null)
            popupStyle2 = PopupWindow(
                    contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true
            )
        }
        popupStyle2?.showAtLocation(btnStyle1, Gravity.BOTTOM, 0, 0)

    }
}
