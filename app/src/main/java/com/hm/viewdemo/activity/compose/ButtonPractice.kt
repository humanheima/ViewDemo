package com.hm.viewdemo.activity.compose

import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ButtonPractice(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(modifier,
        topBar = { SimpleTopAppBarExample("测试Compose Button", onBackClick) }
    ) { padding ->
        //获取Context
        val context = LocalContext.current

        Column(modifier = modifier.padding(padding)) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    Log.i("ButtonPractice", "ButtonPractice: ")
                    Toast.makeText(context, "获取Context hello world", Toast.LENGTH_SHORT).show()
                }) {
                Text(
                    text = "测试Compose Button", fontFamily = FontFamily.Default
                )
            }
            FilledTonalButton(onClick = { }) {
                Text(
                    "Tonal", fontFamily = FontFamily.SansSerif
                )
            }

            OutlinedButton(onClick = { }) {
                Text(
                    "Outlined",
                    fontFamily = FontFamily.Serif
                )
            }

            ElevatedButton(onClick = { }) {
                Text(
                    "Elevated",
                    fontFamily = FontFamily.Monospace
                )
            }

            //文本按钮
            TextButton(
                onClick = { }
            ) {
                Text(
                    "Text Button",
                    fontFamily = FontFamily.Cursive
                )
            }

            useAndroidView()
        }
    }

}


@Composable
private fun useAndroidView() {
    AndroidView(factory = { context ->
        Button(context).apply {

        }
    }, modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
        update = {
            it.text = "使用 AndroidView在 Compose 中嵌套 Android 原生View "
            it.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            it.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        })
}

@Preview(showBackground = true)
@Composable
private fun userAndroidViewPreview() {
    useAndroidView()
}

@Preview(showBackground = true)
@Composable

private fun ButtonPreview() {
    ButtonPractice(Modifier)
}


@Preview(showBackground = true)
@Composable
private fun ButtonPreview2() {
    test()
}


@Composable
private fun test() {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            Log.i("ButtonPractice", "ButtonPractice: ")
        }) {
        Text(
            text = "测试Compose Button", fontFamily = FontFamily.Default
        )
    }
}