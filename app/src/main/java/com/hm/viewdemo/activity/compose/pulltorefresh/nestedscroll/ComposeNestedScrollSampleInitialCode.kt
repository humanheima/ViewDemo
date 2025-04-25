package com.hm.viewdemo.activity.compose.pulltorefresh.nestedscroll

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/**
 * Created by p_dmweidu on 2025/4/25
 * Desc: Compose 嵌套滑动机制，https://medium.com/androiddevelopers/understanding-nested-scrolling-in-jetpack-compose-eb57c1ea0af0
 */
private const val TAG = "ComposeNestedScrollSamp"

// 数据类，表示每项内容（图标 + 文案）
data class ListItem(val icon: ImageVector, val text: String)

// 创建 List 数据
val Contents = listOf(
    ListItem(Icons.Default.ShoppingCart, "Content #0"),
    ListItem(Icons.Default.Person, "Content #1"),
    ListItem(Icons.Default.Email, "Content #2"),
    ListItem(Icons.Default.Person, "Content #3"), // 假设这个图标与 #1 相同
    ListItem(Icons.Default.Favorite, "Content #4"),
    ListItem(Icons.Default.Star, "Content #5"),
    ListItem(Icons.Default.ShoppingCart, "Content #6"),
    ListItem(Icons.Default.Person, "Content #7"),
    ListItem(Icons.Default.Person, "Content #8"),
    ListItem(Icons.Default.Email, "Content #9"),
    ListItem(Icons.Default.Favorite, "Content #10"),
    ListItem(Icons.Default.Star, "Content #11"),
    ListItem(Icons.Default.ShoppingCart, "Content #12"),
    ListItem(Icons.Default.Person, "Content #13"),
    ListItem(Icons.Default.Person, "Content #14"),
    ListItem(Icons.Default.Email, "Content #15"),
    ListItem(Icons.Default.Favorite, "Content #16"),
    ListItem(Icons.Default.Star, "Content #17"),
    ListItem(Icons.Default.ShoppingCart, "Content #18"),
    ListItem(Icons.Default.Person, "Content #19"),
    ListItem(Icons.Default.Person, "Content #20"),
    ListItem(Icons.Default.Email, "Content #21"),
    ListItem(Icons.Default.Favorite, "Content #22"),
    ListItem(Icons.Default.Star, "Content #23"),
    ListItem(Icons.Default.ShoppingCart, "Content #24"),
    ListItem(Icons.Default.Person, "Content #25"),
    ListItem(Icons.Default.Person, "Content #26"),
    ListItem(Icons.Default.Email, "Content #27"),
    ListItem(Icons.Default.Favorite, "Content #28"),
    ListItem(Icons.Default.Star, "Content #29"),
    ListItem(Icons.Default.ShoppingCart, "Content #30"),
    ListItem(Icons.Default.Person, "Content #31"),
    ListItem(Icons.Default.Person, "Content #32"),
    ListItem(Icons.Default.Email, "Content #33"),
    ListItem(Icons.Default.Favorite, "Content #34"),
    ListItem(Icons.Default.Star, "Content #35"),
    ListItem(Icons.Default.ShoppingCart, "Content #36"),
    ListItem(Icons.Default.Person, "Content #37"),
    ListItem(Icons.Default.Person, "Content #38"),
    ListItem(Icons.Default.Email, "Content #39"),


    )

@Composable
fun ListItem(item: ListItem) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {

            Icon(
                imageVector = item.icon, contentDescription = "Localized description"

            )
            Text(
                text = item.text
            )
        }
    }

}

val AppBarHeight = 56.dp
val Purple40 = Color(0xFF6650a4)

/**
 * 包含 NestedScrollConnection
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNestedScrollSampleWithConnection() {

    val appBarOffset by remember { mutableIntStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val connection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    Log.e(TAG, "onPreScroll: available = $available source = $source")
                    return super.onPreScroll(available, source)
                }
            }
        }

        Box(Modifier.nestedScroll(connection)) {
            LazyColumn(contentPadding = PaddingValues(top = AppBarHeight)) {
                items(Contents) {
                    ListItem(item = it)
                }
            }

            TopAppBar(
                modifier = Modifier
                    .height(AppBarHeight)
                    .offset { IntOffset(0, appBarOffset) },
                title = { Text(text = "Jetpack Compose") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = Color.White
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeNestedScrollSampleUsingConnectionClass() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val appBarMaxHeightPx = with(LocalDensity.current) { AppBarHeight.roundToPx() }
        val connection = remember(appBarMaxHeightPx) {
            CollapsingAppBarNestedScrollConnection(appBarMaxHeightPx)
        }

        val density = LocalDensity.current
        val spaceHeight by remember(density) {
            derivedStateOf {
                with(density) {
                    (appBarMaxHeightPx + connection.appBarOffset).toDp()
                }
            }
        }

        Box(Modifier.nestedScroll(connection)) {
            Column {

                Spacer(modifier = Modifier.height(spaceHeight))
                LazyColumn() {
                    items(Contents) {
                        ListItem(item = it)
                    }
                }
            }

            TopAppBar(
                modifier = Modifier
                    .offset { IntOffset(0, connection.appBarOffset) }
                    .height(
                        AppBarHeight
                    ),
                title = { Text(text = "Jetpack Compose") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = Color.White
                )
            )
        }
    }
}

private class CollapsingAppBarNestedScrollConnection(
    val appBarMaxHeight: Int
) : NestedScrollConnection {

    var appBarOffset: Int by mutableIntStateOf(0)
        private set

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y.toInt()
        val newOffset = appBarOffset + delta
        val previousOffset = appBarOffset
        appBarOffset = newOffset.coerceIn(-appBarMaxHeight, 0)
        val consumed = appBarOffset - previousOffset
        return Offset(0f, consumed.toFloat())
    }
}