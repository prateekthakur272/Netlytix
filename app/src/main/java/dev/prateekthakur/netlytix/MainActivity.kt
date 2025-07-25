package dev.prateekthakur.netlytix

import android.os.Bundle
import android.os.TestLooperManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.prateekthakur.netlytix.data.repository.InternetConnectionRepositoryImpl
import dev.prateekthakur.netlytix.data.repository.NetworkConnectionRepositoryImpl
import dev.prateekthakur.netlytix.data.repository.WifiConnectionRepositoryImpl
import dev.prateekthakur.netlytix.domain.repository.InternetConnectionRepository
import dev.prateekthakur.netlytix.ui.theme.NetlytixTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetlytixTheme {
                Test()
            }
        }
    }
}


@Composable
fun Test(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val internet = InternetConnectionRepositoryImpl()
    val network = NetworkConnectionRepositoryImpl(context)
    val wifi = WifiConnectionRepositoryImpl(context)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            wifi.getInfo().collect{
                Log.d("InternetConnection", "${it}")
            }
        }
    }
}