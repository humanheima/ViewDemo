package com.hm.viewdemo.activity.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.hm.viewdemo.databinding.ItemTagComposeBinding

/**
 * Created by p_dmweidu on 2024/10/21
 * Desc: 测试在 Compose 中使用 android 系统的 View
 */
class UseViewInComposeActivity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, UseViewInComposeActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_use_view_in_compose)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        setContent {
            Scaffold(Modifier,
                topBar = { SimpleTopAppBarExample("测试Compose Card", {}) }
            ) { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    ContentExample()
                    AndroidViewBindingExample()

                    ToastGreetingButton("Hello World")

                    AndroidViewInLazyList()
                }
            }
        }

    }


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
                        selectedItem = 1
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

    @Composable
    fun ContentExample() {
        Column(Modifier.fillMaxWidth()) {
            Text("Look at this CustomView!")
            CustomView()
        }
    }


    @Composable
    fun AndroidViewBindingExample() {
        AndroidViewBinding(
            factory = ItemTagComposeBinding::inflate,
            modifier = Modifier.wrapContentHeight(),
            update = {
                tvTagInCompose.text = "Compose inflate xml 布局文件 Hello world"
                // View's been inflated or state read in this block has been updated
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