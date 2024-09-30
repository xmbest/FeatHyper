package me.xmbest.hyper.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import me.xmbest.hyper.R
import me.xmbest.hyper.vm.SystemuiLockViewModule

@Composable
fun SystemuiLockScreen(navController: NavHostController,viewModel: SystemuiLockViewModule = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.systemui_lock),
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(24f, TextUnitType.Sp),
            modifier = Modifier.padding(top = 10.dp, start = 20.dp, bottom = 5.dp).clickable {
                navController.popBackStack()
            }
        )

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 20.dp, bottom = 10.dp)) {
            Text(stringResource(R.string.system_systemui_enable_lock_show_sim), modifier = Modifier.clickable {
                viewModel.updateLockShowSimName(!viewModel.enableLockShowSimName.value)
            })
            Switch(checked = viewModel.enableLockShowSimName.value, onCheckedChange = {
                viewModel.updateLockShowSimName(it)
            }, modifier = Modifier.padding(start = 10.dp))
        }
    }
}