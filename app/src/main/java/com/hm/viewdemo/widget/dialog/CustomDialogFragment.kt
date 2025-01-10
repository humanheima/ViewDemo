package com.hm.viewdemo.widget.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.hm.viewdemo.databinding.DialogSigninBinding


/**
 * Created by dumingwei on 2019-10-22.
 * Desc:
 */
class CustomDialogFragment : DialogFragment() {

    private val TAG = "CustomDialogFragment"

    private var _binding: DialogSigninBinding? = null

    private val mObserver: Observer<LifecycleOwner> = Observer<LifecycleOwner> {
        Log.e(TAG, "lifecycleOwner = ${it.lifecycle.currentState}")
        Log.e(TAG, "showsDialog: = $showsDialog")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewLifecycleOwnerLiveData.observeForever(mObserver)
        Log.e(TAG, "onAttach: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DialogSigninBinding.inflate(inflater, container, false)
        Log.e(TAG, "onCreateView: ")
        return _binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //点击阴影部分和返回键dialog是否消失
        //isCancelable = false

        dialog?.window?.let {
            Log.d(TAG, "onActivityCreated: ")
            it.setGravity(Gravity.BOTTOM)


            //设置宽高全屏
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

            //设置背景透明，不然dialog周围会有padding
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            it.setStatusBarColor(Color.TRANSPARENT);

            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            it.decorView.setSystemUiVisibility(option)
            it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}