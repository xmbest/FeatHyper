package me.xmbest.hyper.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import me.xmbest.hyper.R
import me.xmbest.hyper.utils.AppUtils

/**
 * 应用列表UI
 * @param onNavigate 导航回调
 * @param packageName 应用包名
 * @param iconSize 图标大小
 */
@Composable
fun AppItem(onNavigate: (String) -> Unit, packageName: String, iconSize: Int = 40) {
    val appInfo = AppUtils.getApplicationNameAndIcon(packageName) ?: return
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onNavigate(packageName) }
    ) {
        ListItem(
            headlineContent = {
                Text(text = appInfo.first)
            },
            supportingContent = {
                Text(text = packageName)
            },
            leadingContent = {
                val iconBitmap = appInfo.second?.toBitmap()?.asImageBitmap()
                if (iconBitmap != null) {
                    Image(
                        bitmap = iconBitmap,
                        contentDescription = "${appInfo.first} icon",
                        modifier = Modifier.size(iconSize.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(R.mipmap.ic_launcher_round),
                        contentDescription = "${appInfo.first} icon",
                        modifier = Modifier.size(iconSize.dp)
                    )
                }
            },
            trailingContent = {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.more)
                )
            },
            modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 10.dp, end = 5.dp)
        )
    }
}
