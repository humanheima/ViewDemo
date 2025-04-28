package com.hm.viewdemo.activity.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


/**
 * Created by p_dmweidu on 2025/4/16
 * Desc: compose 下拉刷新，上拉加载的例子，简单例子。
 */
class PullRefreshExampleActivity : AppCompatActivity() {

    companion object {

        fun launch(context: Context) {
            val starter = Intent(context, PullRefreshExampleActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PullRefreshListScreen()
            }
        }
    }
}


// 数据模型
data class PullRefreshItem(val id: Int, val title: String)

// ViewModel
class ListViewModel : ViewModel() {

    private val _items = MutableStateFlow<List<PullRefreshItem>>(emptyList())
    val items: StateFlow<List<PullRefreshItem>> = _items

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private var currentPage = 1

    init {
        refresh() // 初始加载
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // 模拟网络请求
            delay(1000)
            currentPage = 1
            _items.value = fetchItems(currentPage)
            _isRefreshing.value = false
        }
    }

    fun loadMore() {
        if (_isLoadingMore.value || _isRefreshing.value) return
        viewModelScope.launch {
            _isLoadingMore.value = true
            // 模拟网络请求
            delay(1000)
            currentPage++
            _items.value = _items.value + fetchItems(currentPage)
            _isLoadingMore.value = false
        }
    }

    private fun fetchItems(page: Int): List<PullRefreshItem> {
        // 模拟分页数据
        return (1..10).map { PullRefreshItem((page - 1) * 10 + it, "Item ${(page - 1) * 10 + it}") }
    }
}

/**
 * 如果不传递viewModel，则使用 androidx.lifecycle.viewmodel.compose.viewModel() 扩展方法创建一个默认的 ListViewModel。
 *
 * 这样看比较清楚：androidx.lifecycle.viewmodel.compose.viewModel<ListViewModel>()
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefreshListScreen(viewModel: ListViewModel = androidx.lifecycle.viewmodel.compose.viewModel<ListViewModel>()) {
    val items by viewModel.items.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val listState = rememberLazyListState()

    // 下拉刷新状态
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refresh() }
    )

    // 检测是否滚动到底部以加载更多
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItems = layoutInfo.totalItemsCount
                if (lastVisibleItem >= totalItems - 2 && !isLoadingMore && !isRefreshing) {
                    viewModel.loadMore()
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .padding(4.dp)
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = item.title,
                        textAlign = TextAlign.Center
                    )
                }

            }
            if (isLoadingMore) {
                item {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .padding(4.dp)
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Loading more...",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // 下拉刷新指示器
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}