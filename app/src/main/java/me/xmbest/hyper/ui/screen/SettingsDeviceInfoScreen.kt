package me.xmbest.hyper.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import me.xmbest.hyper.cons.SettingsCons
import me.xmbest.hyper.R
import me.xmbest.hyper.utils.SPUtils
import me.xmbest.hyper.vm.SettingsDeviceInfoViewModule

/**
 * 设置-设备详情页
 */
@Composable
fun SettingsDeviceInfoScreen(
    navController: NavHostController,
    viewModel: SettingsDeviceInfoViewModule = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.device_info_edit),
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(24f, TextUnitType.Sp),
            modifier = Modifier.padding(bottom = 10.dp).clickable {
                navController.popBackStack()
            }
        )
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 10.dp)) {
            Text(stringResource(R.string.open), modifier = Modifier.clickable {
                viewModel.updateDeviceEditState(!viewModel.open.value)
            })
            Switch(checked = viewModel.open.value, onCheckedChange = {
                viewModel.updateDeviceEditState(it)
            }, modifier = Modifier.padding(start = 10.dp))
        }
        TextField(
            value = viewModel.deviceName.value,
            onValueChange = {
                viewModel.updateDeviceNameValue(it)
            },
            label = { Text(text = SettingsCons.deviceName.second) },
            modifier = Modifier.fillMaxWidth()
        )

        SettingsCons.deviceInfoMap.forEach { map ->
            var currentValue by rememberSaveable {
                mutableStateOf(
                    SPUtils.getString(
                        map.value,
                        ""
                    ) as String
                )
            }
            TextField(value = currentValue,
                onValueChange = {
                    currentValue = it
                    SPUtils.setString(map.value, it)
                }, label = { Text(text = map.key) }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )
        }
    }
}