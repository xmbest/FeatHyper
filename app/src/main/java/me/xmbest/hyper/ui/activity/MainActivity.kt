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
import me.xmbest.hyper.utils.ResUtils
import me.xmbest.hyper.utils.SPUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSp()
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
     */
    private fun initSp(){
        val initSpSuccess = SPUtils.getInstance().init(this)
        if (!initSpSuccess){
            Toast.makeText(ResUtils.getInstance(),ResUtils.getString(R.string.disable_xposed_tips),Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
