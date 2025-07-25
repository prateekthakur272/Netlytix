package dev.prateekthakur.netlytix.ui.features.networkInfo.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import dev.prateekthakur.netlytix.ui.features.networkInfo.viewmodels.NetworkInfoViewModel
import dev.prateekthakur.netlytix.ui.features.networkInfo.viewmodels.WifiConnectionInfoViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun NetworkDetailsScreen(
    modifier: Modifier = Modifier,
    networkInfoViewModel: NetworkInfoViewModel = koinViewModel(),
    wifiConnectionInfoViewModel: WifiConnectionInfoViewModel = koinViewModel()
) {
    val network by networkInfoViewModel.state.collectAsState()
    val wifi by wifiConnectionInfoViewModel.state.collectAsState()

    Column {
        Text(network.toString())
        Text(wifi.toString())
    }
}