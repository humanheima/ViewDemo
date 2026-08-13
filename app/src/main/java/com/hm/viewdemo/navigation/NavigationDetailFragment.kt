package com.hm.viewdemo.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hm.viewdemo.R

/** 详情目的地：读取 navigation graph 声明的参数，并继续向下一个目的地传参。 */
class NavigationDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_navigation_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemId = requireArguments().getString(NavigationRoutes.ITEM_ID).orEmpty()
        view.findViewById<TextView>(R.id.detail_argument_text).text =
            getString(R.string.navigation_detail_argument, itemId)

        view.findViewById<View>(R.id.open_confirmation_button).setOnClickListener {
            findNavController().navigate(
                R.id.action_navigationDetailFragment_to_navigationConfirmationFragment,
                bundleOf(NavigationRoutes.ITEM_ID to itemId)
            )
        }
    }
}
