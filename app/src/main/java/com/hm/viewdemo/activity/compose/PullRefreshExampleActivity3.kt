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
 * PullRefreshExampleActivity3
 * 简单示例：下拉刷新 + 上拉加载（当某页返回条数 < pageSize 时认为没有更多数据）
 */
class PullRefreshExampleActivity3 : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, PullRefreshExampleActivity3::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val vm: ListViewModel3 = viewModel()
                PullRefreshListScreen3(vm)
            }
        }
    }
}

// 数据模型
data class PullRefreshItem3(
    val id: Int,
    val title: String
)

// UI 状态
data class ListUiState3(
    val items: List<PullRefreshItem3> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true,
    val error: String? = null
)

// ViewModel
class ListViewModel3 : ViewModel() {
    private val _uiState = MutableStateFlow(ListUiState3())
    val uiState: StateFlow<ListUiState3> = _uiState

    private var currentPage = 0
    private var hasMoreData = true
    private val pageSize = 10

    // 模拟总数据量，方便演示（例如 25 条，导致最后一页不足 10 条）
    private val simulatedTotal = 25

    init {
        loadFirstPage()
    }

    fun loadFirstPage() {
        if (_uiState.value.isRefreshing) return
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            try {
                delay(800)
                val items = fetchItems(1)
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
                _uiState.update { it.copy(isRefreshing = false, error = e.message ?: "Unknown") }
            }
        }
    }

    fun loadNextPage() {
        if (!hasMoreData || _uiState.value.isLoadingMore || _uiState.value.isRefreshing) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true, error = null) }
            try {
                delay(800)
                val next = currentPage + 1
                val newItems = fetchItems(next)
                // 当某页返回条数 < pageSize 时认为没有更多数据
                hasMoreData = newItems.size >= pageSize
                _uiState.update {
                    it.copy(
                        items = it.items + newItems,
                        isLoadingMore = false,
                        hasMoreData = hasMoreData
                    )
                }
                currentPage = next
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoadingMore = false, error = e.message ?: "Unknown") }
            }
        }
    }

    private fun fetchItems(page: Int): List<PullRefreshItem3> {
        // 根据 simulatedTotal 生成固定的分页数据，便于复现最后一页不足 pageSize 的情况
        val from = (page - 1) * pageSize
        if (from >= simulatedTotal) return emptyList()
        val toExclusive = minOf(from + pageSize, simulatedTotal)
        val list = (from until toExclusive).map { idx ->
            PullRefreshItem3(id = idx + 1, title = "Item ${idx + 1}")
        }
        return list
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullRefreshListScreen3(viewModel: ListViewModel3 = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val listState = rememberLazyListState()

    val pullState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = { viewModel.loadFirstPage() }
    )

    LaunchedEffect(uiState.error) {
        uiState.error?.let { err ->
            android.widget.Toast.makeText(context, err, android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    // 监听滚动到底部触发加载更多
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                if (layoutInfo.visibleItemsInfo.isNotEmpty() &&
                    !uiState.isRefreshing && !uiState.isLoadingMore && uiState.hasMoreData
                ) {
                    val lastIndex = layoutInfo.visibleItemsInfo.last().index
                    val total = layoutInfo.totalItemsCount
                    if (lastIndex >= total - 2) {
                        viewModel.loadNextPage()
                    }
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize().pullRefresh(pullState)) {
        if (uiState.items.isEmpty() && !uiState.isRefreshing) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No items")
            }
        } else {
            LazyColumn(state = listState, modifier = Modifier.fillMaxSize()) {
                items(uiState.items) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(6.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = item.title, textAlign = TextAlign.Center)
                    }
                }

                if (uiState.items.isNotEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            when {
                                uiState.isLoadingMore -> Text("Loading more...")
                                !uiState.hasMoreData -> Text("No more data")
                            }
                        }
                    }
                }
            }
        }

        PullRefreshIndicator(refreshing = uiState.isRefreshing, state = pullState, modifier = Modifier.align(Alignment.TopCenter))
    }
}

