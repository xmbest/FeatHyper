package me.xmbest.hyper.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import me.xmbest.hyper.R
import me.xmbest.hyper.cons.RouterCons
import me.xmbest.hyper.ui.widget.ListNavigationScreen

@Composable
fun SettingsScreen(onNavigate: (String) -> Unit) {
    ListNavigationScreen(
        title = stringResource(id = R.string.system_settings),
        items = RouterCons.getSettingsList(),
        onNavigate = onNavigate
    )
}

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen {}
}
