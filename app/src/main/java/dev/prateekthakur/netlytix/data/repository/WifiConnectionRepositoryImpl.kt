package dev.prateekthakur.netlytix.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiInfo
import android.os.Build
import androidx.annotation.RequiresApi
import dev.prateekthakur.netlytix.domain.models.WifiConnectionInfo
import dev.prateekthakur.netlytix.domain.repository.WifiConnectionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.net.Inet4Address

class WifiConnectionRepositoryImpl(private val context: Context) : WifiConnectionRepository {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun getInfo(): Flow<WifiConnectionInfo> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                trySend(getWifiInfo(network, networkCapabilities))
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        awaitClose{ connectivityManager.unregisterNetworkCallback(networkCallback)}
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getWifiInfo(network: Network, capabilities: NetworkCapabilities): WifiConnectionInfo {
        val wifiInfo = capabilities.transportInfo as? WifiInfo
        return wifiInfo?.let { info ->
            WifiConnectionInfo().copy(
                ssid = info.ssid,
                bssid = info.bssid,
                linkSpeed = info.linkSpeed,
                frequency = info.frequency,
                gatewayAddress = getGatewayAddress(network),
                ipAddress = getIpAddress(network),
                isSSidHidden = info.hiddenSSID,
                supplicantState = info.supplicantState,
                signalStrength = info.rssi,
                networkId = info.networkId
            )
        } ?: WifiConnectionInfo()
    }

    private fun getGatewayAddress(network: Network): String? {
        val linkProperties = connectivityManager.getLinkProperties(network) ?: return null
        return linkProperties.routes.first { it.isDefaultRoute }.gateway?.hostName
    }

    private fun getIpAddress(network: Network): String? {
        val linkProperties = connectivityManager.getLinkProperties(network) ?: return null
        return linkProperties.linkAddresses.map { it.address }.firstOrNull { it is Inet4Address && !it.isLinkLocalAddress }?.hostName
    }
}
