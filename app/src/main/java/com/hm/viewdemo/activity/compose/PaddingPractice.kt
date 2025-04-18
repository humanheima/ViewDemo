package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Created by p_dmweidu on 2025/4/18
 * Desc: 1. 测试Padding，因为Android 中有 Margin 和 padding。Compose 中的 padding 与 Android 中的 padding 不同。
 *
 * 注意 padding 跟调用顺序是有关系的。
 *
 * @Composable
 * private fun PaddingExample(text: String) {
 *     Box(
 *         modifier = Modifier
 *             .padding(16.dp) // 外边距效果
 *             .background(Color.Gray)
 *             .padding(8.dp) // 内边距
 *             .background(Color.White)
 *     ) {
 *         Text(text)
 *     }
 * }
 *
 */
@Composable
fun PaddingPractice(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(modifier,
        topBar = { SimpleTopAppBarExample("测试LazyColumn", onBackClick) }
    ) { padding ->

        val list = mutableListOf<String>()

        for (i in 0..100) {
            list.add("测试数据 $i")
        }
        LazyColumn(
            modifier = modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            items(
                list
            ) { item ->
                PaddingExample(text = item)
            }
        }
    }
}

/**
 * 这段代码逻辑是
 * 1. 先添加上下16dp的外边距
 * 2.设置背景为灰色
 * 3. 在灰色背景内部添加8dp的内边距
 */
@Composable
private fun PaddingExample(text: String) {
    Box(
        modifier = Modifier
            .padding(16.dp) // 外边距效果
            .background(Color.Gray)
            .padding(8.dp) // 内边距
            .background(Color.White)
    ) {
        Text(text)
    }
}