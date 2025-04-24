package com.hm.viewdemo.activity.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MaterialTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    PagerWithTabs()
//                }
//            }
//        }
//    }
//}

/**
 * Created by p_dmweidu on 2025/4/24
 * Desc: 测试类似 ViewPager + Fragment 的效果。Fragment 内部 加载列表，上拉加载更多。
 */
private const val TAG = "PagerWithTabs"

// ViewModel 管理分页数据
class PagerViewModel : ViewModel() {
    private val _pagesData = MutableStateFlow<List<PageData>>(emptyList())
    val pagesData: StateFlow<List<PageData>> = _pagesData

    init {
        // 初始化三个页面
        _pagesData.value = listOf(
            PageData("Page 1", mutableListOf(), 1, false),
            PageData("Page 2", mutableListOf(), 1, false),
            PageData("Page 3", mutableListOf(), 1, false)
        )
    }

    fun loadMore(pageIndex: Int, onLoadingStateChanged: (Boolean) -> Unit) {
        viewModelScope.launch {
            val currentPages = _pagesData.value.toMutableList()
            val pageData = currentPages.getOrNull(pageIndex)
            if (pageData == null) {
                Log.e(TAG, "loadMore: pageData is null")
                onLoadingStateChanged(false)
                return@launch
            }
            if (pageData.isAllLoaded) {
                Log.e(TAG, "loadMore: all loaded")
                onLoadingStateChanged(false)
                return@launch
            }

            // 设置加载中状态
            onLoadingStateChanged(true)

            // 模拟网络延迟
            delay(1000)

            // 模拟网络返回数据（每页 10 条，第三次加载返回空数据）
            val newItems = if (pageData.currentPage <= 2) {
                (1..10).map { "Item ${pageData.items.size + it} on ${pageData.title}" }
            } else {
                emptyList()
            }

            val updatedPageData = pageData.copy(
                items = (pageData.items + newItems).toMutableList(),
                currentPage = pageData.currentPage + 1,
                isAllLoaded = newItems.isEmpty()
            )

            currentPages[pageIndex] = updatedPageData
            _pagesData.value = currentPages

            // 加载完成后重置加载状态
            onLoadingStateChanged(false)
        }
    }
}

// 页面数据模型
data class PageData(
    val title: String,
    val items: MutableList<String>,
    val currentPage: Int,
    val isAllLoaded: Boolean
)

@Composable
fun PagerWithTabs(modifier: Modifier) {
    val viewModel = remember { PagerViewModel() }
    val pages by viewModel.pagesData.collectAsState()
    val pagerState = rememberPagerState(0, pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        // TabRow
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth()
        ) {
            pages.forEachIndexed { index, page ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(page.title) }
                )
            }
        }

        // HorizontalPager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            PageContent(
                pageData = pages[page],
                onLoadMore = { onLoadingStateChanged ->
                    viewModel.loadMore(page, onLoadingStateChanged)
                }
            )
        }
    }
}

@Composable
fun PageContent(pageData: PageData, onLoadMore: ((Boolean) -> Unit) -> Unit) {
    val listState = rememberLazyListState()
    var isLoading by remember { mutableStateOf(false) }

    // 检测是否滚动到底部
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                val lastVisibleItem = visibleItems.lastOrNull()
                if (lastVisibleItem != null && lastVisibleItem.index >= pageData.items.size - 1 && !pageData.isAllLoaded && !isLoading) {
                    isLoading = true
                    onLoadMore { loading ->
                        isLoading = loading
                        Log.e(TAG, "PageContent: loading = $loading")
                    }
                }
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pageData.items) { item ->
            Text(
                text = item,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp)
            )
        }

        // 显示加载中或全部加载完成
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> CircularProgressIndicator()
                    pageData.isAllLoaded && pageData.items.isNotEmpty() -> Text(
                        text = "加载全部",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )

                    pageData.items.isEmpty() -> Text(
                        text = "暂无数据",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}