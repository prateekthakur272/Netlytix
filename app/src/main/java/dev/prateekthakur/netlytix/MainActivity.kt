package dev.prateekthakur.netlytix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.prateekthakur.netlytix.ui.theme.NetlytixTheme
import dev.prateekthakur.netlytix.ui.navigation.AppNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetlytixTheme {
                AppNavHost()
            }
        }
    }
}
