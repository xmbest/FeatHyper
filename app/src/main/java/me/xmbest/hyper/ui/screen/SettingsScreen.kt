package me.xmbest.hyper.ui.screen

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import me.xmbest.hyper.R
import me.xmbest.hyper.cons.RouterCons


@Composable
fun SettingsScreen(navController: NavHostController?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.system_settings),
            fontWeight = FontWeight.Bold,
            fontSize = TextUnit(24f, TextUnitType.Sp),
            modifier = Modifier.padding(10.dp)
        )
        RouterCons.getSettingsList().forEach {
            ListItem(
                headlineContent = {},
                supportingContent = {
                    Text(text = it.first, fontSize = TextUnit(18f, TextUnitType.Sp))
                },
                trailingContent = {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "more")
                },
                modifier = Modifier
                    .clickable {
                        navController?.navigate(it.second)
                    }
                    .padding(start = 5.dp)
            )
        }
    }

}

@Preview
@Composable
fun PreviewComp() {
    SettingsScreen(null)
}