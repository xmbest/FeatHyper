package me.xmbest.hyper.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.xmbest.hyper.R
import me.xmbest.hyper.cons.RouterCons
import me.xmbest.hyper.ui.widget.ListNavigationScreen

@Composable
fun SystemuiScreen(onNavigate: (String) -> Unit) {
    ListNavigationScreen(
        title = stringResource(id = R.string.system_systemui),
        items = RouterCons.getSystemUiList(),
        onNavigate = onNavigate
    )
}
