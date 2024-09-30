package me.xmbest.hyper.cons

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import me.xmbest.hyper.R
import me.xmbest.hyper.ui.screen.HomeScreen
import me.xmbest.hyper.ui.screen.SettingsDeviceInfoScreen
import me.xmbest.hyper.ui.screen.SettingsScreen
import me.xmbest.hyper.ui.screen.SystemuiScreen
import me.xmbest.hyper.utils.ResUtils

data class RouterPage(val routerName: String, val comp: @Composable (() -> Unit))

class RouterCons {
    companion object {
        /**
         * 程序首页
         */
        const val HOME = "HOME"

        /**
         * 状态栏设置
         */
        private const val SYSTEMUI = "com.android.systemui"

        /**
         * 系统设置
         */
        private const val SETTINGS = "com.android.settings"


        /**
         * 设置-详情页
         */
        private const val SETTINGS_DEVICE_INFO = "SETTINGS_DEVICE_INFO"

        /**
         * 组件列表
         */
        fun getCompList(navController: NavHostController): List<RouterPage> {
            return listOf(
                RouterPage(HOME) { HomeScreen(navController) },
                RouterPage(SYSTEMUI) { SystemuiScreen(navController) },
                RouterPage(SETTINGS) { SettingsScreen(navController) },
                RouterPage(SETTINGS_DEVICE_INFO) { SettingsDeviceInfoScreen(navController) })
        }

        /**
         * 系统设置列表
         */
        fun getSettingsList(): List<Pair<String, String>> {
            return listOf(
                Pair(ResUtils.getString(R.string.settings_device_info), SETTINGS_DEVICE_INFO)
            )
        }
    }
}