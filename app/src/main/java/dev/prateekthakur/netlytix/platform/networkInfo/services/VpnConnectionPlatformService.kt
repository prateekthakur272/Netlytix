package dev.prateekthakur.netlytix.platform.networkInfo.services

import android.content.Context
import android.net.ConnectivityManager
import dev.prateekthakur.netlytix.platform.networkInfo.enums.VpnConnectionType

class VpnConnectionPlatformService(private val context: Context) {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun getVpnType(): VpnConnectionType {
        val interfaces = java.net.NetworkInterface.getNetworkInterfaces().toList()
        val vpnInterface = interfaces.firstOrNull { it.name.startsWith("tun") || it.name.startsWith("ppp") || it.name.contains("ipsec", ignoreCase = true) }

        return when {
            vpnInterface == null -> VpnConnectionType.NONE
            vpnInterface.name.startsWith("tun") -> VpnConnectionType.TUN_BASED
            vpnInterface.name.startsWith("ppp") -> VpnConnectionType.PPP_BASED
            vpnInterface.name.contains("ipsec", ignoreCase = true) -> VpnConnectionType.IPSEC
            else -> VpnConnectionType.UNKNOWN
        }
    }
}