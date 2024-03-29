package com.hm.viewdemo.activity.design

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hm.viewdemo.R
import kotlinx.android.synthetic.main.frag_tablayout.text_fragment

/**
 * Created by dumingwei on 2020/4/28
 *
 *
 * Desc:
 */
class TabLayoutFragment : androidx.fragment.app.Fragment() {

    companion object {

        private const val ARG_PARAM1 = "param1"

        @JvmStatic
        fun newInstance(param1: String?): TabLayoutFragment {
            val fragment = TabLayoutFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            fragment.arguments = args
            return fragment
        }

    }

    private var mParam1: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mParam1 = arguments?.getString(ARG_PARAM1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_fragment.text = mParam1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.frag_tablayout, container, false)
        return view
    }

}