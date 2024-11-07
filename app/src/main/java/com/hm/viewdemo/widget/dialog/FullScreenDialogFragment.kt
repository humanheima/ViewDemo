package com.hm.viewdemo.widget.dialog

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2024/11/7
 * Desc: 全屏的DialogFragment
 */
class FullScreenDialogFragment : DialogFragment() {

    private val TAG = "FullScreenDialogFragmen"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //val bind = FullSCreenDialogBinding.inflate(inflater, container, false)
        val view: View = inflater.inflate(R.layout.full_screen_dialog_frag, container, false)
        return view
        //return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //貌似高版本不调用也可以。多高的版本呢？猜测是5.0以上。
        //dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        dialog?.window?.setBackgroundDrawable(ColorDrawable(0x00000000))
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

}