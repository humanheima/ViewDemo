package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * Created by p_dmweidu on 2025/4/17
 * Desc: 测试 HorizontalPager，类似 ViewPager
 */
@Composable
fun HorizontalPagerPractice(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(modifier,
        topBar = { SimpleTopAppBarExample("测试 HorizontalPager，类似 ViewPager", onBackClick) }
    ) { padding ->

        //SimpleHorizontalPagerSample(modifier = modifier.padding(padding))
        //MyPagerWithIndicator(modifier = modifier.padding(padding))

        val datas = SnapshotStateList<String>()
        DynamicPagerScreen(modifier = modifier.padding(padding), datas = datas)

        // 模拟网络请求
        LaunchedEffect(Unit) {
            // 延迟模拟网络请求
            delay(2000)
            // 更新 items
            datas.addAll(listOf("Item 1", "Item 2", "Item 3"))
        }

    }
}

@Composable
private fun SimpleHorizontalPagerSample(modifier: Modifier) {
    // Creates a 1-pager/viewport horizontal pager with single page snapping
    val state = rememberPagerState { 10 }
    HorizontalPager(
        state = state,
        modifier = modifier.fillMaxSize(),
    ) { page ->
        Box(
            modifier = Modifier
                .padding(10.dp)
                .background(Color.Blue)
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(text = page.toString(), fontSize = 32.sp)
        }
    }
}

@Composable
private fun MyPagerWithIndicator(modifier: Modifier) {
    val pagerState = rememberPagerState(pageCount = { 3 })

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        when (page) {
                            0 -> Color.Blue
                            1 -> Color.Green
                            2 -> Color.Red
                            else -> Color.Gray
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Page ${page + 1}",
                    fontSize = 32.sp,
                    color = Color.White
                )
            }
        }

        // 页面指示器
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { index ->
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .padding(2.dp)
                        .background(
                            color = if (pagerState.currentPage == index) Color.Black else Color.Gray,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
private fun DynamicPagerScreen(modifier: Modifier, datas: SnapshotStateList<String>) {
    // 使用 mutableStateListOf 以支持动态更新
    val items = remember { datas }
    val pagerState = rememberPagerState(pageCount = { items.size })

    // 模拟网络请求
//    LaunchedEffect(Unit) {
//        // 延迟模拟网络请求
//        delay(2000)
//        // 更新 items
//        items.addAll(listOf("Item 1", "Item 2", "Item 3"))
//    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (items.isEmpty()) {
            // 显示加载中或空状态
            Text(text = "Loading...", fontSize = 24.sp)
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            when (page) {
                                0 -> Color.Blue
                                1 -> Color.Green
                                2 -> Color.Red
                                else -> Color.Gray
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = items[page],
                        fontSize = 32.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
