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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ComposePagerActivity
 * 使用 Compose 实现类似 ViewPager + 多个 Fragment 的体验（Horizontal Pager），每页实现下拉刷新 + 上拉加载。
 * 注意：每页的状态由页面作用域的 ViewModel 管理（通过 Compose 的 viewModel factory 和 key 实现）
 */
class ComposePagerActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val starter = Intent(context, ComposePagerActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                PagerScreen()
            }
        }
    }
}

// UI model for items
data class PagerItem(val id: Int, val title: String)

// UI state
data class PagerUiState(
    val items: List<PagerItem> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true,
    val error: String? = null
)

// ViewModel for each page
class PagerPageViewModel(private val pageIndex: Int) : ViewModel() {
    private val _uiState = MutableStateFlow(PagerUiState())
    val uiState: StateFlow<PagerUiState> = _uiState

    private var currentPage = 0
    private var hasMoreData = true
    private val pageSize = 10

    // give each page a simulated total to demonstrate "last page less than pageSize"
    private val simulatedTotal = 18 + pageIndex * 5 // different per page

    init {
        loadFirstPage()
    }

    fun loadFirstPage() {
        if (_uiState.value.isRefreshing) return
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, error = null) }
            try {
                delay(600)
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
                delay(600)
                val next = currentPage + 1
                val newItems = fetchItems(next)
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

    private fun fetchItems(page: Int): List<PagerItem> {
        val from = (page - 1) * pageSize
        if (from >= simulatedTotal) return emptyList()
        val toExclusive = minOf(from + pageSize, simulatedTotal)
        return (from until toExclusive).map { idx ->
            PagerItem(
                id = idx + 1,
                title = "P${pageIndex + 1} Item ${idx + 1}"
            )
        }
    }
}

// Factory to create PagerPageViewModel with pageIndex
class PagerPageViewModelFactory(private val pageIndex: Int) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PagerPageViewModel(pageIndex) as T
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PagerScreen() {
    val pageTitles = listOf("Page 1", "Page 2", "Page 3")
    val pagerState = rememberPagerState(pageCount = { pageTitles.size }, initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    Column {
        // simple TabRow
        androidx.compose.material3.TabRow(selectedTabIndex = pagerState.currentPage) {
            pageTitles.forEachIndexed { index, title ->
                androidx.compose.material3.Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = { Text(title) })
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            // each page is like a Fragment with its own ViewModel
            ComposePagerPageContent(pageIndex = page)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComposePagerPageContent(pageIndex: Int) {
    // create a page-specific ViewModel using a factory and a key so each page keeps its own VM
    val factory = PagerPageViewModelFactory(pageIndex)
    val vm: PagerPageViewModel = viewModel(factory = factory, key = "pager_vm_$pageIndex")
    val uiState by vm.uiState.collectAsState()

    val listState = rememberLazyListState()
    val pullState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = { vm.loadFirstPage() })

    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            android.widget.Toast.makeText(
                context,
                it,
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    // load more when near bottom
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                if (layoutInfo.visibleItemsInfo.isNotEmpty() && !uiState.isRefreshing && !uiState.isLoadingMore && uiState.hasMoreData) {
                    val lastIndex = layoutInfo.visibleItemsInfo.last().index
                    val total = layoutInfo.totalItemsCount
                    if (lastIndex >= total - 2) {
                        vm.loadNextPage()
                    }
                }
            }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .pullRefresh(pullState)) {
        if (uiState.items.isEmpty() && !uiState.isRefreshing) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Page ${pageIndex + 1}: No items")
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

        PullRefreshIndicator(
            refreshing = uiState.isRefreshing,
            state = pullState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
