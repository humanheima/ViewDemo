package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Created by p_dmweidu on 2024/8/28
 * Desc: 可以复制这个模版进行改动
 */
@Composable
fun BadgesPractice(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(modifier,
        topBar = { SimpleTopAppBarExample("测试Compose Badge", onBackClick) }
    ) { padding ->

        //获取Context
        val context = LocalContext.current
        Column(
            modifier = modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            BadgeExample()

            BadgeInteractiveExample(modifier = Modifier.padding(top = 16.dp))

        }
    }
}

@Composable
private fun BadgeExample() {
    BadgedBox(
        badge = {
            Badge()
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Email,
            contentDescription = "Email"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BadgeInteractiveExample(modifier: Modifier = Modifier) {
    var itemCount by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BadgedBox(
            badge = {
                if (itemCount > 0) {
                    Badge(
                        containerColor = Color.Red,//容器颜色
                        contentColor = Color.White//内容颜色
                    ) {
                        Text("$itemCount")
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Shopping cart",
            )
        }
        Button(onClick = { itemCount++ }) {
            Text("Add item")
        }
    }
}

@Preview
@Composable
private fun BadgesPracticeExample() {
    BadgesPractice(modifier = Modifier.padding(16.dp))
}
