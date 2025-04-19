package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.hm.viewdemo.R

@Composable
fun DialogPractice(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(modifier,
        topBar = { SimpleTopAppBarExample("测试Compose Dialog", onBackClick) }
    ) { padding ->

        //获取Context
        val context = LocalContext.current

        val openDialog = remember { mutableStateOf(false) }
        val openCustomDialog = remember { mutableStateOf(false) }
        val openCustomDialog2 = remember { mutableStateOf(false) }
        val openCustomDialog3 = remember { mutableStateOf(false) }

        Column(modifier = modifier.padding(padding)) {

            Button(onClick = {
                openDialog.value = true
            }) {
                Text(text = "显示对话框")
            }
            if (openDialog.value) {
                AlertDialogExample(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    onConfirmation = {
                        openDialog.value = false
                    },
                    dialogTitle = "对话框标题",
                    dialogText = "对话框内容",
                    icon = Icons.Default.Info
                )
            }

            Button(onClick = {
                openCustomDialog.value = true
            }) {
                Text(text = "展示第一个自定义对话框")
            }
            if (openCustomDialog.value) {
                MinimalDialog(onDismissRequest = {
                    openCustomDialog.value = false
                })
            }

            Button(onClick = {
                openCustomDialog2.value = true
            }) {
                Text(text = "展示第二个自定义对话框")
            }
            if (openCustomDialog2.value) {
                DialogWithImage(
                    onDismissRequest = {
                        openCustomDialog2.value = false
                    },
                    onConfirmation = {
                        openCustomDialog2.value = false
                    },
                    //painter = ColorPainter(Color.Cyan),
                    painter = painterResource(R.drawable.balloon),
                    imageDescription = "测试图片"
                )
            }

            Button(onClick = {
                openCustomDialog3.value = true
            }) {
                Text(text = "展示第3个自定义对话框")
            }
            if (openCustomDialog3.value) {
                CustomDialogExample(openCustomDialog3)
            }
        }
    }
}


@Composable
private fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}


@Composable
fun CustomDialogExample(openDialog: MutableState<Boolean>) {

    if (openDialog.value) {
        Dialog(
            onDismissRequest = { openDialog.value = false },

            //dismissOnBackPress：是否允许通过返回键关闭。
            //dismissOnClickOutside：是否允许点击外部关闭。
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
        ) {
            // 自定义对话框内容
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                //.wrapContentHeight(Alignment.Bottom),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("自定义对话框", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("这里可以放置任何 Compose UI 组件。")
                    Spacer(modifier = Modifier.height(16.dp))
                    Row {
                        Button(onClick = { openDialog.value = false }) {
                            Text("关闭")
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun MinimalDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "This is a minimal dialog",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun DialogWithImage(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    painter: Painter,
    imageDescription: String,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painter,
                    contentDescription = imageDescription,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(160.dp)
                )
                Text(
                    text = "This is a dialog with buttons and an image.",
                    modifier = Modifier.padding(16.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}