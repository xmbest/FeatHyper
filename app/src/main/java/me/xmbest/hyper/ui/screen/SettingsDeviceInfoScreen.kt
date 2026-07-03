package me.xmbest.hyper.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.xmbest.hyper.cons.SettingsCons
import me.xmbest.hyper.R
import me.xmbest.hyper.ui.widget.SwitchSettingRow
import me.xmbest.hyper.utils.SPUtils
import me.xmbest.hyper.vm.SettingsDeviceInfoViewModel

/**
 * 设置-设备详情页
 */
@Composable
fun SettingsDeviceInfoScreen(
    onBack: () -> Unit,
    viewModel: SettingsDeviceInfoViewModel = viewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.device_info_edit),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(start = 20.dp, bottom = 10.dp, top = 10.dp)
        )

        SwitchSettingRow(
            label = stringResource(R.string.enable),
            checked = viewModel.enable.value,
            onCheckedChange = { viewModel.updateDeviceEditState(it) }
        )

        SettingsCons.deviceInfoMap.forEach { map ->
            var currentValue by rememberSaveable {
                mutableStateOf(SPUtils.getString(map.value, ""))
            }
            TextField(
                value = currentValue,
                onValueChange = {
                    currentValue = it
                    SPUtils.setString(map.value, it)
                },
                label = { Text(text = map.key) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            )
        }
    }
}
