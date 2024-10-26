package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Created by p_dmweidu on 2024/8/28
 * Desc: 可以复制这个模版进行改动
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
