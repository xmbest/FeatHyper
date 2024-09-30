package me.xmbest.hyper.ui.widget

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import me.xmbest.hyper.R
import me.xmbest.hyper.utils.AppUtils


private val TAG = "AppItem"

/**
 * 应用列表UI
 * @author xmbest
 * @date 2024/09/23
 * @param packageName 应用包名
 * @param iconSize 图标大小
 */
@Composable
fun AppItem(navController: NavHostController?, packageName: String, iconSize:Int = 40) {
    val app = AppUtils.getApplicationNameAndIcon(packageName)
    app?.let {
        Row(modifier = Modifier.fillMaxWidth().clickable {
            Log.d(TAG,"AppItem.click packageName = $packageName")
            navController?.let {
                navController.navigate(packageName)
            }
        }) {
            ListItem(
                headlineContent = {
                    Text(text = app.first)
                },
                supportingContent = {
                    Text(text = packageName)
                },
                leadingContent = {
                    if (null == app.second){
                        Image(painter = painterResource(R.mipmap.ic_launcher_round), contentDescription = "${app.first} icon", modifier = Modifier.size(iconSize.dp))
                    }else{
                        Image(bitmap = app.second?.toBitmap()!!.asImageBitmap(), contentDescription = "${app.first} icon", modifier = Modifier.size(iconSize.dp))
                    }
                },
                trailingContent = {
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = "more")
                },
                modifier = Modifier.padding(start = 10.dp, top = 5.dp, bottom = 10.dp, end = 5.dp)
            )
        }
    }
}