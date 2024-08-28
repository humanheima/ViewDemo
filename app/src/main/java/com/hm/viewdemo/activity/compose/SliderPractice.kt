package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

/**
 * Created by p_dmweidu on 2024/8/28
 * Desc: 可以复制这个模版进行改动
 */
@Composable
fun SliderPractice(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {
    Scaffold(modifier,
        topBar = { SimpleTopAppBarExample("测试Compose Slider", onBackClick) }
    ) { padding ->

        //获取Context
        val context = LocalContext.current
        Column(modifier = modifier.padding(padding)) {

            SliderMinimalExample()

            SliderAdvancedExample()

            RangeSliderExample()

        }
    }
}

@Preview
@Composable
private fun SliderMinimalExample() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Column {
        //默认选择范围是 0f - 1f
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it }
        )
        Text(text = sliderPosition.toString())
    }
}

@Preview
@Composable
fun SliderAdvancedExample() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    Column {
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            //除了起始点和结束点，可选择值数量
            steps = 8,
            valueRange = 0f..100f
        )
        Text(text = sliderPosition.toString())
    }
}

@Preview
@Composable
fun RangeSliderExample() {
    var sliderPosition by remember { mutableStateOf(0f..100f) }
    Column {
        RangeSlider(
            value = sliderPosition,
            steps = 5,
            onValueChange = { range -> sliderPosition = range },
            valueRange = 0f..100f,
            onValueChangeFinished = {
                // launch some business logic update with the state you hold
                // viewModel.updateSelectedSliderValue(sliderPosition)
            },
        )
        Text(text = sliderPosition.toString())
    }
}