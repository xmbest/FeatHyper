package me.xmbest.hyper.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import me.xmbest.hyper.cons.RouterCons

@Composable
fun Router(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RouterCons.HOME,
        modifier = modifier
    ) {

        RouterCons.getCompList(navController).forEach { page ->
            composable(page.routerName) {
                page.comp.invoke()
            }
        }
    }
}