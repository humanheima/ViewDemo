package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WidgetsEnter(
    modifier: Modifier,
    itemList: List<Item>,
    onBackClick: () -> Unit
) {

    Scaffold(
        topBar = { SimpleTopAppBarExample("Widgets 入口", onBackClick) }
    ) { padding ->
        LazyColumn(modifier = modifier.padding(padding)) {
            items(itemList) { item ->
                CustomItem(item)
            }
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