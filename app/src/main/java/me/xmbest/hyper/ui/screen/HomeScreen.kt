package me.xmbest.hyper.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.xmbest.hyper.ui.widget.AppItem
import me.xmbest.hyper.R
import me.xmbest.hyper.vm.HomeViewModel

/**
 * 首页
 */
@Composable
fun HomeScreen(onNavigate: (String) -> Unit, viewModel: HomeViewModel = viewModel()) {
    Column {
        Text(
            text = stringResource(R.string.app_list),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(viewModel.getPackageNameList(), key = { it }) { packageName ->
                AppItem(onNavigate = onNavigate, packageName = packageName)
            }
        }
    }
}
