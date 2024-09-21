package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Created by p_dmweidu on 2024/9/21
 * Desc: 测试Modifier
 *
 * 使用父类的传递过来的 Modifier 和 自己新创建的 Modifier 好像没区别？
 *
 */
@Composable
fun ModifierTest(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(16.dp)) {

        Button(
            modifier = modifier.padding(start = 16.dp).fillMaxWidth(),
            onClick = {
            }) {
            Text(
                text = "使用父类的Modifier", fontFamily = FontFamily.Default
            )
        }

        Button(
            modifier = Modifier.padding(start = 16.dp).fillMaxWidth(),
            onClick = {
            }) {
            Text(
                text = "使用自己的Modifier", fontFamily = FontFamily.Default
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun ModifierTestPreview() {
    ModifierTest(Modifier)
}