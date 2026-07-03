package me.xmbest.hyper.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.xmbest.hyper.R
import me.xmbest.hyper.ui.widget.SwitchSettingRow
import me.xmbest.hyper.vm.SystemuiLockViewModel

@Composable
fun SystemuiLockScreen(onBack: () -> Unit, viewModel: SystemuiLockViewModel = viewModel()) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.systemui_lock),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 10.dp, start = 20.dp, bottom = 5.dp)
        )

        SwitchSettingRow(
            label = stringResource(R.string.system_systemui_enable_lock_show_sim),
            checked = viewModel.enableLockShowSimName.value,
            onCheckedChange = { viewModel.updateLockShowSimName(it) }
        )
    }
}
