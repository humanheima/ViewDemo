package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
public fun FloatButtonPractice(
    modifier: Modifier, onBackClick: () -> Unit = {},
    onClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = { SimpleTopAppBarExample(title = "测试 悬浮操作按钮", navigateBack = onBackClick) }
    ) { padding ->

        Column(
            modifier = modifier
                .padding(padding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            SmallFloatingActionButton(
                modifier = modifier.padding(8.dp),
                onClick = { onClick() },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Filled.Add, "Small floating action button.")
            }

            FloatingActionButton(
                modifier = modifier.padding(8.dp),

                onClick = { onClick() },
            ) {
                Icon(Icons.Default.Add, "Floating action button.")
            }

            LargeFloatingActionButton(
                modifier = modifier.padding(8.dp),

                onClick = { onClick() },
                shape = CircleShape,
            ) {
                Icon(Icons.Filled.Add, "Large floating action button")
            }

            ExtendedFloatingActionButton(
                modifier = modifier.padding(8.dp),

                onClick = { onClick() },
                icon = { Icon(Icons.Filled.Edit, "Extended floating action button.") },
                text = { Text(text = "Extended FAB") },
            )
        }


    }
}


