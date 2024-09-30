package me.xmbest.hyper.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import me.xmbest.hyper.ui.Router
import me.xmbest.hyper.ui.theme.FeatHyperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}
