package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ButtonPractice(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(modifier,
        topBar = { SimpleTopAppBarExample("测试Compose Button", onBackClick) }
    ) { padding ->

        Column(modifier = modifier.padding(padding)) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { }) {
                Text(text = "测试Compose Button")
            }
            FilledTonalButton(onClick = { }) {
                Text("Tonal")
            }

            OutlinedButton(onClick = { }) {
                Text("Outlined")
            }

            ElevatedButton(onClick = { }) {
                Text("Elevated")
            }

            //文本按钮
            TextButton(
                onClick = { }
            ) {
                Text("Text Button")
            }
        }
    }

}