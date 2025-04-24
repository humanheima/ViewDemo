package com.hm.viewdemo.activity.compose.scrolltest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.hm.viewdemo.activity.compose.SimpleTopAppBarExample
import kotlin.math.roundToInt

/**
 * Created by p_dmweidu on 2025/4/24
 * Desc: 滚动相关知识，走马观花
 */
@Composable
fun ScrollPractice(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(modifier,
        topBar = { SimpleTopAppBarExample("测试Compose 滚动相关的知识点", onBackClick) }
    ) { padding ->

        //ScrollBoxes()
        //ScrollBoxesSmooth()
        //ScrollableSample()
        //AutomaticNestedScroll(modifier = Modifier.padding(padding))
        //NestedScrollDispatcherSample(modifier = Modifier.padding(padding))
        ImageResizeOnScrollExample(modifier = Modifier.padding(padding))
    }
}

@Composable
private fun ScrollBoxes() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .size(200.dp)
            .verticalScroll(rememberScrollState())
    ) {
        repeat(20) {
            Text("Item $it", modifier = Modifier.padding(2.dp))
        }
    }
}

@Composable
private fun ScrollBoxesSmooth() {
    val state = rememberScrollState()
    // Smoothly scroll 100px on first composition，但是后面就滚动不下来了。
    LaunchedEffect(Unit) { state.animateScrollTo(100) }

    Column(
        modifier = Modifier
            .background(Color.LightGray)
            .size(200.dp)
            .padding(horizontal = 8.dp)
            .verticalScroll(state)
    ) {
        repeat(20) {
            Text("Item $it", modifier = Modifier.padding(2.dp))
        }
    }
}

/**
 * 注意这里用的是 rememberScrollableState，上面用的是 rememberScrollState
 */
@Composable
private fun ScrollableSample() {
    // actual composable state
    var offset by remember { mutableFloatStateOf(0f) }
    Box(
        Modifier
            .size(200.dp)
            .fillMaxWidth()
            .scrollable(
                orientation = Orientation.Vertical,
                // Scrollable state: describes how to consume
                // scrolling delta and update offset
                state = rememberScrollableState { delta ->
                    offset += delta
                    delta
                }
            )
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(offset.toString())
    }
}


/**
 * 感觉例子不大对，滑动起来不流畅
 */
@Composable
fun AutomaticNestedScroll(modifier: Modifier) {
    val gradient = Brush.verticalGradient(0f to Color.Gray, 1000f to Color.White)
    Box(
        modifier = modifier
            .background(Color.LightGray)
            .verticalScroll(rememberScrollState())
            .padding(32.dp)
            .height(400.dp)
    ) {
        Column {
            repeat(6) {
                Box(
                    modifier = Modifier
                        .height(128.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "Scroll here",
                        modifier = Modifier
                            .border(12.dp, Color.DarkGray)
                            .background(brush = gradient)
                            .padding(24.dp)
                            .height(150.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun NestedScrollDispatcherSample(modifier: Modifier) {
    // Let's take Modifier.draggable (which doesn't have nested scroll build in, unlike Modifier
    // .scrollable) and add nested scroll support our component that contains draggable

    // this will be a generic components that will work inside other nested scroll components.
    // put it inside LazyColumn or / Modifier.verticalScroll to see how they will interact

    // first, state and it's bounds
    val basicState = remember { mutableStateOf(0f) }
    val minBound = -100f
    val maxBound = 100f
    // lambda to update state and return amount consumed
    val onNewDelta: (Float) -> Float = { delta ->
        val oldState = basicState.value
        val newState = (basicState.value + delta).coerceIn(minBound, maxBound)
        basicState.value = newState
        newState - oldState
    }
    // create a dispatcher to dispatch nested scroll events (participate like a nested scroll child)
    val nestedScrollDispatcher = remember { NestedScrollDispatcher() }

    // create nested scroll connection to react to nested scroll events (participate like a parent)
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                // we have no fling, so we're interested in the regular post scroll cycle
                // let's try to consume what's left if we need and return the amount consumed
                val vertical = available.y
                val weConsumed = onNewDelta(vertical)
                return Offset(x = 0f, y = weConsumed)
            }
        }
    }
    Box(
        modifier = modifier
            .size(100.dp)
            .background(Color.LightGray)
            .fillMaxWidth()
            // attach ourselves to nested scroll system
            .nestedScroll(connection = nestedScrollConnection, dispatcher = nestedScrollDispatcher)
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    // here's regular drag. Let's be good citizens and ask parents first if they
                    // want to pre consume (it's a nested scroll contract)
                    val parentsConsumed = nestedScrollDispatcher.dispatchPreScroll(
                        available = Offset(x = 0f, y = delta),
                        source = NestedScrollSource.Drag
                    )
                    // adjust what's available to us since might have consumed smth
                    val adjustedAvailable = delta - parentsConsumed.y
                    // we consume
                    val weConsumed = onNewDelta(adjustedAvailable)
                    // dispatch as a post scroll what's left after pre-scroll and our consumption
                    val totalConsumed = Offset(x = 0f, y = weConsumed) + parentsConsumed
                    val left = adjustedAvailable - weConsumed
                    nestedScrollDispatcher.dispatchPostScroll(
                        consumed = totalConsumed,
                        available = Offset(x = 0f, y = left),
                        source = NestedScrollSource.Drag
                    )
                    // we won't dispatch pre/post fling events as we have no flinging here, but the
                    // idea is very similar:
                    // 1. dispatch pre fling, asking parents to pre consume
                    // 2. fling (while dispatching scroll events like above for any fling tick)
                    // 3. dispatch post fling, allowing parent to react to velocity left
                }
            )
    ) {
        Text(
            "State: ${basicState.value.roundToInt()}",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

/**
 * 类似一个嵌套滑动简单的例子，触摸图片是滑动不了的。
 */
@Composable
fun ImageResizeOnScrollExample(
    modifier: Modifier = Modifier,
    maxImageSize: Dp = 300.dp,
    minImageSize: Dp = 100.dp
) {
    var currentImageSize by remember { mutableStateOf(maxImageSize) }
    var imageScale by remember { mutableFloatStateOf(1f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Calculate the change in image size based on scroll delta
                val delta = available.y
                val newImageSize = currentImageSize + delta.dp
                val previousImageSize = currentImageSize

                // Constrain the image size within the allowed bounds
                currentImageSize = newImageSize.coerceIn(minImageSize, maxImageSize)
                val consumed = currentImageSize - previousImageSize

                // Calculate the scale for the image
                imageScale = currentImageSize / maxImageSize

                // Return the consumed scroll amount
                return Offset(0f, consumed.value)
            }
        }
    }

    Box(Modifier.nestedScroll(nestedScrollConnection)) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
                //图片大小变化的时候，偏移量也会变化
                .offset {
                    IntOffset(0, currentImageSize.roundToPx())
                }
        ) {
            // Placeholder list items
            items(100, key = { it }) {
                Text(
                    text = "Item: $it",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Image(
            painter = ColorPainter(Color.Red),
            contentDescription = "Red color image",
            Modifier
                .size(maxImageSize)
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    scaleX = imageScale
                    scaleY = imageScale
                    // Center the image vertically as it scales
                    translationY = -(maxImageSize.toPx() - currentImageSize.toPx()) / 2f
                }
        )
    }
}

@Preview
@Composable
private fun BadgesPracticeExample() {
    ScrollPractice(modifier = Modifier.padding(16.dp))
}
