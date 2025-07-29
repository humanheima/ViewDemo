package com.hm.viewdemo.widget.dialog

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowCompat
import androidx.fragment.app.DialogFragment
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2024/11/7
 * Desc: 全屏的DialogFragment，使用theme 来实现
 */
class FullScreenDialogFragmentUseTheme : DialogFragment() {

    private val TAG = "FullScreenDialogFragmen"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.full_screen_dialog_frag, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        /**
         * 如果要延伸到状态栏，可以使用这个来实现
         */
        hideSystemUI()
    }

    private fun hideSystemUI() {
        //让内容延伸到状态栏上
        dialog?.window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
            //注释1处，设置状态栏的背景颜色
            //it.statusBarColor = Color.TRANSPARENT
            WindowCompat.getInsetsController(it, it.decorView).let { controller ->
                //注释2处，false，状态栏上的颜色是白色的。true ，状态栏上的颜色是黑色的。
                controller.isAppearanceLightStatusBars = true
            }
        }
    }
}