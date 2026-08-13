package com.hm.viewdemo.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hm.viewdemo.R

/**
 * 首页目的地：使用 action 将参数传给详情页。
 *
 * 这里故意使用 Bundle，便于展示不依赖 Safe Args 插件时的最小实现方式。
 */
class NavigationHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_navigation_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.open_detail_button).setOnClickListener {
            val args = bundleOf(NavigationRoutes.ITEM_ID to "android-navigation")
            findNavController().navigate(R.id.action_navigationHomeFragment_to_navigationDetailFragment, args)
        }
    }
}
