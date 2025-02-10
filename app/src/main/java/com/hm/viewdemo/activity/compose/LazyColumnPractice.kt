package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Created by p_dmweidu on 2024/8/28
 * Desc: 可以复制这个模版进行改动
 *
 * 1. 测试LazyColumn
 *
 */
@Composable
fun LazyColumnPractice(
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
                Box(
                    modifier = Modifier.fillMaxSize(), // 填充整个可用空间
                    contentAlignment = Alignment.Center // 设置内容居中
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge // 使用主题的文本样式
                    )
                }
            }
        }
    }
}
