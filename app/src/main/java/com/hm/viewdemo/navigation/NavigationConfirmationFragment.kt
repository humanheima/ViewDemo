package com.hm.viewdemo.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hm.viewdemo.R

/**
 * 末级目的地：popBackStack 到首页，演示一次性清理中间页面。
 */
class NavigationConfirmationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_navigation_confirmation, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemId = requireArguments().getString(NavigationRoutes.ITEM_ID).orEmpty()
        view.findViewById<TextView>(R.id.confirmation_argument_text).text =
            getString(R.string.navigation_confirmation_argument, itemId)

        view.findViewById<View>(R.id.back_to_home_button).setOnClickListener {
            // inclusive=false 保留首页，只移除首页之上的详情和确认页。
            //findNavController().popBackStack(R.id.navigationHomeFragment, false)
            findNavController().popBackStack(R.id.navigationHomeFragment, false)
        }
    }
}

internal object NavigationRoutes {
    const val ITEM_ID = "item_id"
}
