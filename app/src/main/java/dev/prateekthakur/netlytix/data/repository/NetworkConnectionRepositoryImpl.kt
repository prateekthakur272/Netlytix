package dev.prateekthakur.netlytix.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import dev.prateekthakur.netlytix.domain.enums.NetworkConnectionType
import dev.prateekthakur.netlytix.domain.models.NetworkConnectionInfo
import dev.prateekthakur.netlytix.domain.repository.NetworkConnectionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NetworkConnectionRepositoryImpl(context: Context) : NetworkConnectionRepository {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun getInfo(): Flow<NetworkConnectionInfo> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                trySend(getNetworkInfo(network, networkCapabilities))
            }
        }

        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        awaitClose{connectivityManager.unregisterNetworkCallback(networkCallback)}
    }

    private fun getNetworkInfo(network: Network, capabilities: NetworkCapabilities) : NetworkConnectionInfo{
        val linkProperties: LinkProperties? = connectivityManager.getLinkProperties(network)
        return NetworkConnectionInfo(
            vpnConnected = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN),
            internetAccessible = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET),
            dnsServer = linkProperties?.dnsServers?.map { it.hostName } ?: emptyList(),
            type = getNetworkType(capabilities)
        )
    }

    private fun getNetworkType(capabilities: NetworkCapabilities) : NetworkConnectionType {
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkConnectionType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkConnectionType.CELLULAR
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkConnectionType.ETHERNET
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> NetworkConnectionType.VPN
            else -> NetworkConnectionType.NONE
        }
    }
}