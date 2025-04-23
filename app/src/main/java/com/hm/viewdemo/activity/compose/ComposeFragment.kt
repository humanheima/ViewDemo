import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hm.viewdemo.activity.compose.PullRefreshViewPagerScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Created by p_dmweidu on 2025/4/23
 * Desc: 测试下拉刷新，上拉加载相关。类似 RecyclerView + PullRefresh + ViewPager里面嵌套多个Fragment
 */
class ComposeFragment : Fragment() {


    companion object {
        fun newInstance(): ComposeFragment {
            val args = Bundle()
            val fragment = ComposeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
               // MainScreen(Modifier.fillMaxSize())
                PullRefreshViewPagerScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(modifier: Modifier, viewModel: MainViewModel = viewModel()) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh = {
        viewModel.refresh()
    })

    Box(
        modifier = modifier
            .pullRefresh(pullRefreshState)
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

        LazyColumn(
            modifier = Modifier
                .padding(top = 52.dp)
                .fillMaxSize()
                .background(Color.White)
                //.verticalScroll(outerScrollState)
                //.nestedScroll(nestedScrollConnection)
        ) {
            item {
                // 模拟其他内容
                Text(
                    text = "Header Content",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )

                // 外层内容
                repeat(10) {
                    Text(
                        text = "Outer Item $it",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

            }
            item {
                // 嵌套 HorizontalPager
                PagerSection(viewModel)
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

@Composable
fun PagerSection(viewModel: MainViewModel) {
    val tabs = listOf("Tab 1", "Tab 2", "Tab 3")
    val pagerState = rememberPagerState(0, pageCount = {
        tabs.size
    })

    Column(modifier = Modifier.height(500.dp)) {
        // TabRow
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = pagerState.currentPage == index,
                    onClick = { /* 切换 Tab */ }
                )
            }
        }
        // HorizontalPager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) { page ->
            PagerContent(page, viewModel)
        }
    }
}

@Composable
fun PagerContent(page: Int, viewModel: MainViewModel) {
    val items by viewModel.getPageItems(page).collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items) { item ->
            Text(
                text = "Item $item",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
        if (isLoadingMore) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .wrapContentWidth()
                )
            }
        }
    }

    // 检测上拉加载更多
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                if (lastVisibleItem != null && lastVisibleItem.index >= items.size - 2) {
                    coroutineScope.launch {
                        viewModel.loadMore(page)
                    }
                }
            }
    }
}

// ViewModel 管理数据
class MainViewModel : ViewModel() {

    private val _page = MutableStateFlow(0)
    val page: StateFlow<Int> = _page
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private val pageItems =
        List(3) { MutableStateFlow(List(20) { index -> "${it + 1}-${index + 1}" }) }

    fun getPageItems(page: Int): StateFlow<List<String>> = pageItems[page]

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            delay(1000) // 模拟网络请求
            pageItems.forEachIndexed { index, flow ->
                flow.value = List(20) { "page = ${page.value} 第 ${index + 1} 个 tab -${it + 1}" }
            }
            _isRefreshing.value = false
            _page.value++
        }
    }

    fun loadMore(page: Int) {
        viewModelScope.launch {
            if (_isLoadingMore.value) return@launch
            _isLoadingMore.value = true
            delay(1000) // 模拟网络请求
            val currentItems = pageItems[page].value
            val newItems = currentItems + List(10) { "${page + 1}-${currentItems.size + it + 1}" }
            pageItems[page].value = newItems
            _isLoadingMore.value = false
        }
    }
}