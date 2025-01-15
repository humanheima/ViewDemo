package com.hm.viewdemo.widget.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.hm.viewdemo.R

/**
 * Created by p_dmweidu on 2025/1/10
 * Desc: 给 DialogFragment 设置 出现时候的动画
 */
class AnimateInDialogFragment : DialogFragment() {

    override fun onStart() {
        super.onStart()
        //getDialog()?.getWindow()?.setWindowAnimations(R.style.DialogAnimation);
        dialog?.window?.setWindowAnimations(R.style.scale_animate_dialog);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.animate_in_dialog_frag, container, false)
    }
}