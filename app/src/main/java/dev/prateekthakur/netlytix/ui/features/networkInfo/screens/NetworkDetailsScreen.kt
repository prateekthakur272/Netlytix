package dev.prateekthakur.netlytix.ui.features.networkInfo.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.prateekthakur.netlytix.R
import dev.prateekthakur.netlytix.domain.enums.NetworkConnectionType
import dev.prateekthakur.netlytix.ui.features.networkInfo.viewmodels.MobileNetworkInfoViewModel
import dev.prateekthakur.netlytix.ui.features.networkInfo.viewmodels.NetworkInfoViewModel
import dev.prateekthakur.netlytix.ui.features.networkInfo.viewmodels.WifiConnectionInfoViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.R)
@RequiresPermission("android.permission.READ_PRIVILEGED_PHONE_STATE")
@Composable
fun NetworkDetailsScreen(
    modifier: Modifier = Modifier,
    networkInfoViewModel: NetworkInfoViewModel = koinViewModel(),
) {
    val scrollState = rememberScrollState()
    Scaffold() { safeArea ->
        Column(
            modifier = modifier
                .padding(safeArea)
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(state = scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BasicNetworkDetailsView(networkInfoViewModel)
            NetworkTestButton()
            WifiNetworkInfoView()
            MobileConnectionInfoView()
        }
    }
}

@Composable
fun InfoRow(title: String, value: String, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(title, style = MaterialTheme.typography.labelLarge)
        Text(value, style = MaterialTheme.typography.labelLarge.copy(Color.Gray))
    }
}

@Composable
fun NetworkTestButton(modifier: Modifier = Modifier) {
    Card {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Run Network Tests", style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = {}) { Icon(Icons.Default.PlayArrow, contentDescription = "Test") }
        }
    }
}

@Composable
fun BasicNetworkDetailsView(
    networkInfoViewModel: NetworkInfoViewModel,
    modifier: Modifier = Modifier
) {

    val info by networkInfoViewModel.state.collectAsState()
    val isConnected = info.type != NetworkConnectionType.NONE

    Card {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text("Connection Status", style = MaterialTheme.typography.labelMedium)
            Text(
                if (isConnected) "Connected" else "Disconnected",
                style = MaterialTheme.typography.titleMedium
            )

            val pulseAnimation = rememberInfiniteTransition()
            val scale by pulseAnimation.animateFloat(
                initialValue = 1f, targetValue = 1.1f, animationSpec = infiniteRepeatable(
                    animation = tween(800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxWidth()) {

                Image(
                    painter = painterResource(
                        id = if (info.type == NetworkConnectionType.NONE) {
                            R.drawable.not_connected
                        } else {
                            R.drawable.connected
                        }
                    ), contentDescription = null, modifier = Modifier
                        .size(160.dp)
                        .graphicsLayer(
                            scaleX = scale, scaleY = scale
                        )
                )
            }

            Text(info.type.name, style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(modifier = modifier.padding(vertical = 8.dp))

            InfoRow("Internet", if (!info.internetAccessible) "Disconnected" else "Connected")
            InfoRow("VPN", if (!info.vpnConnected) "Disconnected" else "Connected")
            InfoRow("Gateway", info.dnsServer.joinToString { "$it " })

        }
    }
}

@Composable
fun WifiNetworkInfoView(
    modifier: Modifier = Modifier,
    wifiConnectionInfoViewModel: WifiConnectionInfoViewModel = koinViewModel()
) {
    val info by wifiConnectionInfoViewModel.state.collectAsState()
    Card {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Text("Wifi Details", style = MaterialTheme.typography.titleMedium)

            Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(R.drawable.wifi),
                    contentDescription = "",
                    modifier = modifier.size(120.dp)
                )
            }

            Spacer(modifier = modifier.height(12.dp))

            InfoRow("SSID", info.ssid ?: "Unknown")
            InfoRow("BSSID", info.bssid ?: "Unknown")
            InfoRow("Strength", "${info.signalStrength} dBm")
            InfoRow("Hidden", info.isSSidHidden.toString())
            InfoRow("Network ID", info.networkId.toString())
            InfoRow("Frequency", "${info.frequency} Hz")
            InfoRow("Link Speed", "${info.linkSpeed} Mbps")
            InfoRow("Gateway", info.gatewayAddress ?: "Unknown")
            InfoRow("Supplicant State", info.supplicantState?.name ?: "Unknown")
        }
    }
}

@RequiresPermission("android.permission.READ_PRIVILEGED_PHONE_STATE")
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun MobileConnectionInfoView(
    modifier: Modifier = Modifier,
    mobileNetworkInfoViewModel: MobileNetworkInfoViewModel = koinViewModel()
) {
    val info by mobileNetworkInfoViewModel.state.collectAsState()

    Card {
        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text("Mobile Network Details", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = modifier.height(16.dp))
            info?.let { info ->
                InfoRow("Signal Strength", info.signalStrength?.level?.toString() ?: "")
                InfoRow("Sim Operator", "${info.simOperatorName} ${info.simOperator}")
                InfoRow("Network Operator", "${info.networkOperatorName} ${info.networkOperator}")
                InfoRow("Active Modems", "${info.activeModemCount}")
                Spacer(modifier = modifier.height(16.dp))
            }
        }
    }
}