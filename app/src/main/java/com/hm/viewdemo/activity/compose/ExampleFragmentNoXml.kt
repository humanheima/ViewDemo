package com.hm.viewdemo.activity.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment

/**
 * Created by p_dmweidu on 2024/10/26
 * Desc: 整个布局层级都是 Compose
 */
class ExampleFragmentNoXml : Fragment() {


    companion object {
        fun newInstance(): ExampleFragmentNoXml {
            val args = Bundle()
            val fragment = ExampleFragmentNoXml()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    // In Compose world
                    Text("Hello Compose!")
                    Surface(color = MaterialTheme.colorScheme.primary) {
                        Text("整个布局层级都是Compose")
                    }
                }
            }
        }
    }
}