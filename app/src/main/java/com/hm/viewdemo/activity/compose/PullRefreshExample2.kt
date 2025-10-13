package com.hm.viewdemo.activity.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Created by p_dmweidu on 2025/4/16
 * Desc: Compose 下拉刷新和上拉加载更多示例，支持第一页数据不足时禁用加载更多
 */
class PullRefreshExampleActivity2 : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, PullRefreshExampleActivity2::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val viewModel: ListViewModel2 = viewModel()
                PullRefreshListScreen2(viewModel = viewModel)
            }
        }
    }
}

// 数据模型
data class PullRefreshItem2(
    val id: Int,
    val title: String
)

// UI状态
data class ListUiState2(
    val items: List<PullRefreshItem2> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true,
    val error: String? = null
)

// ViewModel
class ListViewModel2 : ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState2())
    val uiState: StateFlow<ListUiState2> = _uiState

    private var currentPage = 0
    private var hasMoreData = true
    private val pageSize = 10

    init {
        loadFirstPage()
    }

    fun loadFirstPage() {
        if (_uiState.value.isRefreshing) return

        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            try {
                delay(1000) // 模拟网络请求
                val items = fetchItems(1) // 第一页
                hasMoreData = items.size >= pageSize
                _uiState.update {
                    it.copy(
                        items = items,
                        isRefreshing = false,
                        isLoadingMore = false,
                        hasMoreData = hasMoreData
                    )
                }
                currentPage = 1
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun loadNextPage() {
        if (!hasMoreData || _uiState.value.isLoadingMore || _uiState.value.isRefreshing) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true, error = null) }
            try {
                delay(1000) // 模拟网络请求
                val nextPage = currentPage + 1
                val newItems = fetchItems(nextPage)
                
                // 如果返回的条数小于10条，认为没有更多数据了
                hasMoreData = newItems.size >= 10
                _uiState.update {
                    it.copy(
                        items = _uiState.value.items + newItems,
                        isLoadingMore = false,
                        hasMoreData = hasMoreData
                    )
                }
                currentPage = nextPage
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoadingMore = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    private fun fetchItems(page: Int): List<PullRefreshItem2> {
        // 模拟数据，每页返回随机数量（1-10条）
        val count = (1..10).random()
        return (1..count).map {
            PullRefreshItem2(
                id = (page - 1) * pageSize + it,
                title = "Item ${(page - 1) * pageSize + it}"
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefreshListScreen2(
    viewModel: ListViewModel2 = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    // 下拉刷新状态
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = { viewModel.loadFirstPage() }
    )

    // 显示错误信息
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            android.widget.Toast.makeText(context, error, android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    // 检测是否滚动到底部以加载更多
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                if (layoutInfo.visibleItemsInfo.isNotEmpty() &&
                    !uiState.isRefreshing &&
                    !uiState.isLoadingMore &&
                    uiState.hasMoreData
                ) {
                    val lastVisibleItem = layoutInfo.visibleItemsInfo.last().index
                    val totalItems = layoutInfo.totalItemsCount
                    
                    // 当滚动到倒数第二个项目时加载更多
                    if (lastVisibleItem >= totalItems - 2) {
                        viewModel.loadNextPage()
                    }
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        if (uiState.items.isEmpty() && !uiState.isRefreshing) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No items available")
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                // 列表项
                items(uiState.items) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp)
                            .padding(4.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.title,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // 加载更多指示器或没有更多数据提示
                if (uiState.items.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                uiState.isLoadingMore -> Text("Loading more...")
                                !uiState.hasMoreData -> Text("No more data")
                            }
                        }
                    }
                }
            }
        }

        // 下拉刷新指示器
        PullRefreshIndicator(
            refreshing = uiState.isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
