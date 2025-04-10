package com.hm.viewdemo.activity.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.viewdemo.R

@Composable
fun LazyColumnPractice2(
    modifier: Modifier,
    onBackClick: () -> Unit = {}
) {

//    Scaffold(modifier,
//        topBar = { SimpleTopAppBarExample("测试LazyColumn2", onBackClick) }
//    ) { padding ->
//        Row {
//            Spacer(
//                modifier = Modifier
//                    .width(16.dp)
//                    .padding(padding)
//            )
//        }
//
//        MembershipScreen()
//
//    }
    MembershipScreen()

}

// 数据模型
data class MembershipItem(
    val iconRes: Int,
    val title: String,
    val description: String,
    val isHidden: Boolean,
    var selected: Boolean = false
)

@Composable
fun MembershipScreen() {
    // 动态数据列表
    val membershipItems = listOf(
        MembershipItem(
            iconRes = android.R.drawable.star_big_on,
            title = "新人体验型 (系统赠型)",
            description = "每期天一轮消耗1星，日升卡/VIP/SVIP 畅聊",
            isHidden = false,
            selected = true
        ),
        MembershipItem(
            iconRes = android.R.drawable.ic_menu_info_details,
            title = "剧情体验型",
            description = "你还比剧情简单，连合全剧情，每期天一轮消耗3星，日升卡/VIP/SVIP 畅聊",
            isHidden = false
        ),
        MembershipItem(
            iconRes = android.R.drawable.ic_menu_help,
            title = "你聊天体验型",
            description = "你帮我一轮将体验，连合你用用，日升卡/VIP/SVIP 畅聊",
            isHidden = true
        ),
        MembershipItem(
            iconRes = android.R.drawable.ic_menu_view,
            title = "深度思考 付费",
            description = "限时体验，日升卡/VIP/SVIP 畅聊。注：该体验型出时间较长，日升卡/VIP/SVIP 畅聊",
            isHidden = true
        ),
        MembershipItem(
            iconRes = android.R.drawable.ic_menu_help,
            title = "深度思考 (深度思考体验)",
            description = "限时体验，日升卡/VIP/SVIP 畅聊。V1.3.20以上版本生效，注：该体验型出时间较长",
            isHidden = true
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // 顶部标题
        Text(
            text = "日升卡/VIP/SVIP 限时体验",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 使用 LazyColumn 渲染动态列表
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(membershipItems) { item ->
                MembershipCard(
                    iconRes = item.iconRes,
                    title = item.title,
                    description = item.description,
                    isHidden = item.isHidden,
                    isSelected = item.selected
                )
            }
        }

        // 底部取消按钮
        Button(
            onClick = { /* 取消逻辑 */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF5F5F5),
                contentColor = Color.Black
            )
        ) {
            Text(text = "取消", fontSize = 16.sp)
        }
    }
}

@Composable
fun MembershipCard(
    iconRes: Int,
    title: String,
    description: String,
    isHidden: Boolean,
    isSelected: Boolean,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        border = if (isSelected) {
            BorderStroke(
                width = 1.5.dp,
                color = Color(0xFF8A4AF3) // 选中时显示 1dp 紫色边框
            )
        } else {
            null // 未选中时无边框
        }
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_select_mode_flag),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.End)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {

            Row {
                // 图标
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                if (isHidden) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "付费",
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color(0xFF8A4AF3), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}