package com.hm.viewdemo.widget.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.FullScreenDialogFragBinding

/**
 * Created by p_dmweidu on 2025/7/29
 * Desc: 这种方式设置全屏的DialogFragment
 */
class FullScreenDialog3 : DialogFragment() {

    companion object {

        const val TYPE_TO_OPEN = 1
        const val TYPE_TO_CLOSE = 2
        const val ARG_ACTION_TYPE = "arg_action_type"

        @JvmStatic
        fun newInstance(type: Int): FullScreenDialog3 {
            val args = Bundle()
            args.putInt(ARG_ACTION_TYPE, type)
            val fragment = FullScreenDialog3()
            fragment.arguments = args
            return fragment
        }
    }

    private var type = TYPE_TO_OPEN
    private var binding: FullScreenDialogFragBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        val window = dialog.window
        val decorView = window?.decorView!!
        val controller = WindowCompat.getInsetsController(window!!, decorView)

        controller.show(WindowInsetsCompat.Type.statusBars())
        // 同时隐藏状态栏和导航栏
        //controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.setAppearanceLightStatusBars(true)

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        type = arguments?.getInt(ARG_ACTION_TYPE) ?: TYPE_TO_OPEN
        binding = FullScreenDialogFragBinding.inflate(inflater, container, false)

        return binding?.root
    }

}
