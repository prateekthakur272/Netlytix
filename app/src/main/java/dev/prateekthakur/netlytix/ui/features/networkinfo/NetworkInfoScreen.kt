package dev.prateekthakur.netlytix.ui.features.networkinfo

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlin.math.roundToLong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetworkInfoScreen(viewModel: NetworkInfoViewModel = hiltViewModel()) {
    val state = viewModel.state.collectAsState().value
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text("Netlytix")
            }
        )
    }) { scaffoldPAdding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(count = 2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            modifier = Modifier.padding(scaffoldPAdding).padding(8.dp)
        ) {
            item { ConnectionInfoView(state) }
            item { InternetConnectionView(state) }
            item { VpnConnectionView(state) }
            item { PhysicalInfoView(state) }
        }
    }
}

@Composable
fun NetworkInfoCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
            .padding(16.dp)
    ) {
        content()
    }
}

@Composable
fun NetworkInfoRow(title: String, value: String, modifier: Modifier = Modifier) {
    Column {
        Text(text = title, style = MaterialTheme.typography.labelMedium)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ConnectionInfoView(state: NetworkInfoState, modifier: Modifier = Modifier) {
    NetworkInfoCard {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.Info, contentDescription = "Info")
            NetworkInfoRow("Connection Type", state.connectionType.name)
            NetworkInfoRow("SSID", state.ssid)
            NetworkInfoRow("BSSID", state.bssid)
            NetworkInfoRow("IP address(v4)", state.ipv4)
            NetworkInfoRow("IP address(v6)", state.ipv6)
            NetworkInfoRow("Public Ip", state.publicIp)
            NetworkInfoRow("Link speed(mbps)", state.linkSpeedMbps.toString())
            NetworkInfoRow("Signal Strength", state.signalStrength.toString())
            NetworkInfoRow("IP address(v4)", state.gateway)
        }
    }
}

@Composable
fun InternetConnectionView(state: NetworkInfoState, modifier: Modifier = Modifier) {
    NetworkInfoCard {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.Warning, contentDescription = "Info")
            NetworkInfoRow("Internet", state.isInternetAccessible.toString())
            NetworkInfoRow("Upload speed(kbps)", "${state.uploadSpeedKbps.roundToLong()} Kbps")
            NetworkInfoRow("Download speed", "${state.downloadSpeedKbps.roundToLong()} Kbps")
            NetworkInfoRow("Ping", "${state.pingLatencyMs} ms")
            NetworkInfoRow("Packet loss", "${state.packetLossPercent}%")
            NetworkInfoRow("DNS", (state.dnsServers).joinToString { "$it " })
        }
    }
}

@Composable
fun VpnConnectionView(state: NetworkInfoState, modifier: Modifier = Modifier) {
    NetworkInfoCard {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.Warning, contentDescription = "Info")
            NetworkInfoRow("VPN", state.isVpnActive.toString())
            NetworkInfoRow("VPN type", state.vpnType)
        }
    }
}



@Composable
fun PhysicalInfoView(state: NetworkInfoState, modifier: Modifier = Modifier) {
    NetworkInfoCard {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.Warning, contentDescription = "Info")
            NetworkInfoRow("MAC address", state.macAddress)
        }
    }
}