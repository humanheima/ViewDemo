package com.hm.viewdemo.activity.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import com.hm.viewdemo.databinding.ComposeInFragmentExampleBinding

/**
 * Created by p_dmweidu on 2024/10/21
 * Desc:
 */
class UseComposeInFragment : Fragment() {

    private var _binding: ComposeInFragmentExampleBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!


    companion object {

        fun newInstance(): UseComposeInFragment {
            val args = Bundle()
            val fragment = UseComposeInFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ComposeInFragmentExampleBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // In Compose world
                MaterialTheme {
                    Greeting(name = "Android")
                }
            }
        }
        return view
    }

    @Composable
    fun Greeting(name: String) {
        // Text(text = "Hello $name!")
        Column {
            Text(text = "Hello Compose $name!")
            Text("在 Fragment 中使用 Compose， Hello Compose!")
            Button(onClick = { }) {
                Text(text = "Button 测试Compose Widget")
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        Greeting("Android")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}