package me.xmbest.hyper.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import me.xmbest.hyper.R
import me.xmbest.hyper.ui.Router
import me.xmbest.hyper.ui.theme.FeatHyperTheme
import me.xmbest.hyper.utils.SPUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!initSp()) return
        enableEdgeToEdge()
        setContent {
            FeatHyperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Router(Modifier
                        .padding(innerPadding).fillMaxWidth())
                }
            }
        }
    }

    /**
     * 初始化SharedPreferences
     * @return true 初始化成功，false 初始化失败
     */
    private fun initSp(): Boolean {
        val initSpSuccess = SPUtils.init(this)
        if (!initSpSuccess) {
            Toast.makeText(this, R.string.disable_xposed_tips, Toast.LENGTH_LONG).show()
            finish()
        }
        return initSpSuccess
    }
}
