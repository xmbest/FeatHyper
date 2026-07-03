package me.xmbest.hyper.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.xmbest.hyper.R

/**
 * 通用列表导航页面
 * @param title 页面标题
 * @param items 列表项，Pair<显示文本, 路由名称>
 * @param onNavigate 导航回调
 */
@Composable
fun ListNavigationScreen(
    title: String,
    items: List<Pair<String, String>>,
    onNavigate: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
        )
        items.forEach { item ->
            ListItem(
                headlineContent = {
                    Text(text = item.first, fontSize = 18.sp)
                },
                trailingContent = {
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.more)
                    )
                },
                modifier = Modifier
                    .clickable { onNavigate(item.second) }
                    .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
            )
        }
    }
}
