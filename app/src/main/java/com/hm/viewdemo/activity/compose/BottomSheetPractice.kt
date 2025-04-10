package com.hm.viewdemo.activity.compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * Created by p_dmweidu on 2024/8/28
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomSheetPractice(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        modifier,
        topBar = {
            SimpleTopAppBarExample(
                "测试Compose BottomSheet",
                onBackClick
            )
        })
    { padding ->
        ModalBottomSheetExample(modifier = Modifier.padding(padding))
        // 按钮触发显示 BottomSheet
//        Button(
//            modifier = Modifier.padding(padding),
//            onClick = { }) {
//            Text("显示 ModalBottomSheet")
//        }

        //ModalBottomSheetExample()
//        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
//        val scope = rememberCoroutineScope()
//        var showBottomSheet by remember { mutableStateOf(false) }
//        Scaffold(
//            modifier = Modifier.padding(padding),
//            floatingActionButton = {
//                ExtendedFloatingActionButton(
//                    text = { Text("Show bottom sheet") },
//                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
//                    onClick = {
//                        showBottomSheet = true
//                    }
//                )
//            }
//        ) { contentPadding ->
//            if (showBottomSheet) {
//                ModalBottomSheet(
//                    modifier = Modifier
//                        .padding(),
//                    onDismissRequest = {
//                        showBottomSheet = false
//                    },
//                    sheetState = sheetState
//
//                ) {
//                    // Sheet content
//                    Button(onClick = {
//                        scope.launch { sheetState.hide() }.invokeOnCompletion {
//                            if (!sheetState.isVisible) {
//                                showBottomSheet = false
//                            }
//                        }
//                    }) {
//                        Text("Hide bottom sheet")
//                    }
//                }
//            }
//
//            PartialBottomSheet()
//        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetExample(modifier: Modifier) {
    // 控制 BottomSheet 的状态
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    // 按钮触发显示 BottomSheet
    Button(modifier = modifier,
        onClick = { showSheet = true }) {
        Text("显示 ModalBottomSheet")
    }

    // ModalBottomSheet 组件
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false }, // 点击外部或返回时关闭
            sheetState = sheetState
        ) {
            // BottomSheet 的内容
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    //加上这一个，不然会盖到底部虚拟状态栏上
                    //.padding(WindowInsets.safeDrawing.asPaddingValues()),
                    //这个属性也可以
                    .padding(WindowInsets.safeContent.asPaddingValues()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "这是一个 ModalBottomSheet",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text("你可以在这里添加任何内容，例如按钮、文本或表单。")
                Button(onClick = {
                    scope.launch { sheetState.hide() } // 手动隐藏 BottomSheet
                    showSheet = false
                }) {
                    Text("关闭")
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PartialBottomSheet() {
    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { showBottomSheet = true }
        ) {
            Text("Display partial bottom sheet")
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),//最大高度
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                Text(
                    "Swipe up to open sheet. Swipe down to dismiss.",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
@Preview
private fun BottomSheetPracticePreview() {
    BottomSheetPractice(modifier = Modifier)
}


