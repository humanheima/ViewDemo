package com.hm.viewdemo.widget.dialog

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hm.viewdemo.R

class TopSlideDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置DialogFragment的样式
        setStyle(STYLE_NO_TITLE, R.style.TopSlideDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // 这里使用你的布局文件
        val view = inflater.inflate(R.layout.fragment_top_slide_dialog, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View?) {
        val btnClose = view?.findViewById<View>(R.id.btn_close)
        btnClose?.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        // 设置Dialog的宽度和高度
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        //设置居顶部
        dialog?.window?.setGravity(Gravity.TOP)

        // 添加从顶部滑入的动画
        //dialog?.window?.setWindowAnimations(R.style.TopSlideAnimation)
    }
}