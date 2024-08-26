package com.hm.viewdemo.activity.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Created by p_dmweidu on 2024/8/24
 * Desc: 测试Compose中的Widget入口Activity
 */
class WidgetsActivity : AppCompatActivity() {


    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, WidgetsActivity::class.java)
            context.startActivity(starter)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val itemList = mutableListOf(
            Item("item1") {
                Toast.makeText(this, "item1", Toast.LENGTH_SHORT).show()
            },
            Item("item2") {
                Toast.makeText(this, "item2", Toast.LENGTH_SHORT).show()
            }
        )
//        val itemList2 = mutableListOf<Item>()
//
//        for (i in 0..100) {
//            itemList2.add(Item("item$i") {
//                Toast.makeText(this, "item$i", Toast.LENGTH_SHORT).show()
//            })
//        }

        setContent {
//            MaterialTheme {
//                Surface {
//                    LazyColumn {
//                        items(itemList) { item ->
//                            CustomItem(item)
//                        }
//                    }
//                }
//            }

            ScaffoldExample(itemList = itemList, navigateBack = { finish() })
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScaffoldExample(itemList: List<Item>, navigateBack: () -> Unit) {

    var presses by remember { mutableIntStateOf(0) }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBarExample(scrollBehavior = scrollBehavior,
                navigateBack = {
                    navigateBack()
                }
            )
        },
        bottomBar = {
            BottomAppBarExample()
        },
        floatingActionButton = {
            Button(onClick = {
                presses++
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text =
                """
                    This is an example of a scaffold. It uses the Scaffold composable's parameters to create a screen with a simple top app bar, bottom app bar, and floating action button.

                    It also contains some basic inner content, such as this text.

                    You have pressed the floating action button $presses times.
                """.trimIndent(),
            )

            CustomItemList(itemList = itemList)

        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CenterAlignedTopAppBarExample(
    scrollBehavior: TopAppBarScrollBehavior,
    navigateBack: () -> Unit
) {

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),

        title = {
            Text(
                text = "Centered Top App Bar",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Localized description")
            }
        },
        scrollBehavior = scrollBehavior

    )

}

@Composable
private fun BottomAppBarExample() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = { /* do something */ }) {
                Icon(Icons.Filled.Check, contentDescription = "Localized description")
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.MailOutline,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Info,
                    contentDescription = "Localized description",
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomAppBarPreview() {
    BottomAppBarExample()
}

@Composable
private fun CustomItemList(itemList: List<Item>) {
    LazyColumn {
        items(itemList) { item ->
            CustomItem(item)
        }
    }
}

@Composable
private fun CustomItem(item: Item) {
    Button(
        onClick = {
            item.onclick()
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Text(text = item.text)
    }
}

data class Item(val text: String, val onclick: () -> Unit)