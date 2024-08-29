package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
 * Desc: 可以复制这个模版进行改动
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetPractice(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(modifier,
        topBar = { SimpleTopAppBarExample("测试Compose BottomSheet", onBackClick) }
    ) { padding ->
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember { mutableStateOf(false) }
        Scaffold(
            modifier = Modifier.padding(padding),
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Show bottom sheet") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                    onClick = {
                        showBottomSheet = true
                    }
                )
            }
        ) { contentPadding ->
            if (showBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier.padding(contentPadding),
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    // Sheet content
                    Button(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    }) {
                        Text("Hide bottom sheet")
                    }
                }
            }

            PartialBottomSheet()
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


