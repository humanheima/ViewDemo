package com.hm.viewdemo.activity.compose

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.fragment.app.Fragment
import com.hm.viewdemo.R
import com.hm.viewdemo.databinding.ItemTagComposeBinding
import com.hm.viewdemo.databinding.ItemXmlViewComposeBinding
import com.hm.viewdemo.databinding.MyFragmentLayoutBinding

/**
 * Created by p_dmweidu on 2024/10/21
 * Desc: 测试在 Compose 中使用 android 系统的 View
 */
class UseViewInComposeActivity : AppCompatActivity() {


    companion object {

        private const val TAG = "UseViewInComposeActivit"

        fun launch(context: Context) {
            val starter = Intent(context, UseViewInComposeActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(Modifier, topBar = {
                SimpleTopAppBarExample("在 Compose 中使用 View") {
                    finish()
                }
            }
            ) { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    MHomeScreen()
                    ContentExample()

                    CustomViewGroup()

                    AndroidViewBindingExample()

                    FragmentInComposeExample()

                    ToastGreetingButton("Hello World")

                    AndroidViewInLazyList()
                }
            }
        }
    }

    /**
     * 返回一个单独的View
     */
    @Composable
    fun CustomView() {
        var selectedItem by remember { mutableStateOf(0) }

        // Adds view to Compose
        AndroidView(
            modifier = Modifier.fillMaxWidth(), // Occupy the max size in the Compose UI tree
            factory = { context ->
                // Creates view
                Button(context).apply {
                    // Sets up listeners for View -> Compose communication
                    setOnClickListener {
                        selectedItem++
                    }
                }
            },
            update = { view ->
                // View's been inflated or state read in this block has been updated
                // Add logic here if necessary

                // As selectedItem is read here, AndroidView will recompose
                // whenever the state changes
                // Example of Compose -> View communication
                view.text = selectedItem.toString()
            }
        )
    }


    /**
     * 返回一个ViewGroup
     */
    @Composable
    fun CustomViewGroup() {
        var selectedItem by remember { mutableStateOf(0) }

        // Adds view to Compose
        AndroidView(
            modifier = Modifier.fillMaxWidth(), // Occupy the max size in the Compose UI tree
            factory = { context ->
                // Creates view
                LinearLayout(context).apply {


//                    layoutParams = LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        LinearLayout.LayoutParams.WRAP_CONTENT
//                    )

                    orientation = LinearLayout.VERTICAL

                    background = getDrawable(R.drawable.bg_shape_corner_16)

                    addView(
                        TextView(context).apply {
                            text = "AndroidView 中的原生 第一个子View Hello World"
                            setTextColor(getColor(R.color.colorAccent))
                        },
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    )

                    addView(Button(context).apply {
                        id = R.id.btn_btn_in_compose_android_view
                        setOnClickListener {
                            selectedItem += 2
                        }
                    })

                }
            },
            update = { view ->
                Log.d(TAG, "CustomView2: parentView = ${view.parent}")
                view.findViewById<Button>(R.id.btn_btn_in_compose_android_view).text =
                    selectedItem.toString()
            }
        )
    }

    @Composable
    fun ContentExample() {
        Column(Modifier.fillMaxWidth()) {
            Text("Look at this CustomView!")
            CustomView()
        }
    }


    @Composable
    fun AndroidViewBindingExample() {
        AndroidViewBinding(factory = { inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean ->
            ItemXmlViewComposeBinding.inflate(inflater, parent, attachToParent)
        },
            modifier = Modifier.wrapContentHeight(),
            update = {
                findViewById<TextView>(R.id.tv_tag_in_compose).text =
                    "Compose inflate xml 布局文件 Hello world"
            })
    }

    @Composable
    fun AndroidViewInLazyList() {
        LazyColumn {
            items(100) { index ->
                AndroidView(
                    modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
                    factory = { context ->
                        Button(context)
                    },
                    update = { view ->
                        view.text = "第 $index 个按钮"
                    },
                    onReset = { view ->

                    }
                )
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun ContentExamplePreview() {
        ContentExample()
    }

    @Composable
    fun ToastGreetingButton(greeting: String) {
        val context = LocalContext.current
        Button(onClick = {
            Toast.makeText(context, greeting, Toast.LENGTH_SHORT).show()
        }) {
            Text("Greet")
        }
    }

}

@Composable
fun FragmentInComposeExample() {
    AndroidViewBinding(MyFragmentLayoutBinding::inflate) {
        val myFragment = fragmentContainerView.getFragment<MyFragment>()
        // ...
    }
}

class MyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_scanner_anim, container, false)
        return view
    }
}

@Composable
fun SystemBroadcastReceiver(
    systemAction: String,
    onSystemEvent: (intent: Intent?) -> Unit
) {
    // Grab the current context in this part of the UI tree
    val context = LocalContext.current

    // Safely use the latest onSystemEvent lambda passed to the function
    val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)

    // If either context or systemAction changes, unregister and register again
    DisposableEffect(context, systemAction) {
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                currentOnSystemEvent(intent)
            }
        }

        context.registerReceiver(broadcast, intentFilter)

        // When the effect leaves the Composition, remove the callback
        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}

@Composable
fun MHomeScreen() {
    SystemBroadcastReceiver(Intent.ACTION_BATTERY_CHANGED) { batteryStatus ->
        val isCharging = /* Get from batteryStatus ... */ true

        Log.d("UseViewInComposeActivit", "isCharging: = $isCharging")
        /* Do something if the device is charging */
    }

    /* Rest of the HomeScreen */
}
