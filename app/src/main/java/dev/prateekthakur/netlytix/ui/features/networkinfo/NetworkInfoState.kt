package dev.prateekthakur.netlytix.ui.features.networkinfo

import dev.prateekthakur.netlytix.domain.models.ConnectedNetworkType

data class NetworkInfoState(
    val connectionType: ConnectedNetworkType = ConnectedNetworkType.UNKNOWN,
    val ssid: String = "Unknown",
    val bssid: String = "Unknown",
    val ipv4: String = "0.0.0.0",
    val ipv6: String = "::",
    val gateway: String = "0.0.0.0",
    val dnsServers: List<String> = emptyList(),
    val macAddress: String = "Unavailable",

    val signalStrength: Int = -1,
    val linkSpeedMbps: Int = -1,
    val uploadSpeedKbps: Double = 0.0,
    val downloadSpeedKbps: Double = 0.0,
    val pingLatencyMs: Long = -1L,
    val packetLossPercent: Double = 0.0,

    val isInternetAccessible: Boolean = false,
    val isVpnActive: Boolean = false,
    val vpnType: String = "None",
    val publicIp: String = "0.0.0.0"
)