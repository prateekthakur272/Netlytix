package dev.prateekthakur.netlytix.domain.models

import dev.prateekthakur.netlytix.domain.enums.NetworkConnectionType

data class NetworkConnectionInfo(
    val type: NetworkConnectionType = NetworkConnectionType.NONE,
    val internetAccessible: Boolean = false,
    val vpnConnected: Boolean = false,
    val dnsServer: List<String> = emptyList(),
)