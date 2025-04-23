package com.hm.viewdemo.activity.compose

import MainViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by p_dmweidu on 2025/4/23
 * Desc: 下拉刷新，上拉加载，但是顶部滑动不了。
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefreshViewPagerScreen(viewModel: MainViewModel = viewModel()) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // 管理下拉刷新状态
    val refreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        // 模拟刷新操作
        coroutineScope.launch {
            delay(2000)
            // 刷新完成后更新状态
        }
    })

    // ViewPager 的状态
    val pagerState = rememberPagerState(0, pageCount = { 3 })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState)
    ) {

        NestedScrollView(
            modifier = Modifier.fillMaxSize(),
            content = {
                // 顶部区域
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.LightGray),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "顶部区域", style = MaterialTheme.typography.h6)
                }

                // ViewPager 区域
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        //.fillMaxHeight() 使用这个属性报错。
                        .height(400.dp)

                ) { page ->
                    // 每个页面的内容
                    PageContent(page = page)
                }
            }
        )

        // 下拉刷新指示器
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun PageContent(page: Int) {
    // 管理上拉加载状态
    val listState = rememberLazyListState()
    val items = remember { mutableStateListOf((1..20).map { "Item $it" }) }
    val isLoading = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // 检测是否滑动到底部以触发加载更多
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()
                if (lastVisibleItem != null && lastVisibleItem.index >= items.size - 2 && !isLoading.value) {
                    isLoading.value = true
                    // 模拟加载更多
                    coroutineScope.launch {
                        delay(2000)
                        items.addAll(listOf((1..10).map { "New Item ${items.size + it}" }))
                        isLoading.value = false
                    }
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items) { item ->
            Text(
                text = "Page $page: $item",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(8.dp)
            )
        }
        if (isLoading.value) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

// 自定义 NestedScrollView 以处理外部和内部滑动
@Composable
fun NestedScrollView(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {


    val outerScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // 外层未滚动到顶部时，优先消耗垂直滚动
                val isAtTop = outerScrollState.value == 0
                return if (!isAtTop && available.y != 0f) {
                    coroutineScope.launch {
                        outerScrollState.scrollBy(-available.y)
                    }
                    available
                } else {
                    Offset.Zero
                }
            }
        }
    }


    Column(
        modifier = modifier
            .verticalScroll(outerScrollState)
            .nestedScroll(nestedScrollConnection)
    ) {
        content()
    }
}