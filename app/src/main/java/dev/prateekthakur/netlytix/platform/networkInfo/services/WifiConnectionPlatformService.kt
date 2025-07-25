package dev.prateekthakur.netlytix.platform.networkInfo.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.InetAddress

class WifiConnectionPlatformService(private val context: Context){

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun getWifiInfo(): Flow<WifiInfo?> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                val wifiInfo = networkCapabilities.transportInfo as? WifiInfo
                trySend(wifiInfo)
            }
        }
        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
    }.flowOn(Dispatchers.IO)

    fun getWifiLinkSpeedMbps(): Flow<Int?> = getWifiInfo().map { wifiInfo -> wifiInfo?.linkSpeed }

    fun getWifiSignalStrength(): Flow<Int?> = getWifiInfo().map { info ->
        return@map when{
            info == null -> null
            info.rssi >= -50 -> 4     // Excellent
            info.rssi >= -60 -> 3     // Good
            info.rssi >= -70 -> 2     // Fair
            info.rssi >= -80 -> 1     // Weak
            else -> 0                 // Very Weak
        }
    }

    suspend fun getWifiSSID(): String? = getWifiInfo().first()?.ssid

    suspend fun getWifiBSSID(): String? = getWifiInfo().first()?.bssid

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun getGatewayAddress(): InetAddress? = withContext(Dispatchers.IO) {
        try {
            val activeNetwork = connectivityManager.activeNetwork ?: return@withContext null
            val linkProperties = connectivityManager.getLinkProperties(activeNetwork) ?: return@withContext null

            val gateway = linkProperties.routes.first { it.isDefaultRoute }.gateway
            return@withContext gateway
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun getIpAddress(): InetAddress? = withContext(Dispatchers.IO) {
        try {
            val activeNetwork = connectivityManager.activeNetwork ?: return@withContext null
            val linkProperties = connectivityManager.getLinkProperties(activeNetwork) ?: return@withContext null
            val ipAddress = linkProperties.linkAddresses
                .map { it.address }
                .firstOrNull { it is Inet4Address && !it.isLinkLocalAddress }
            return@withContext ipAddress
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getNetworkId(): Int? = withContext(Dispatchers.IO) { getWifiInfo().firstOrNull()?.networkId }

    suspend fun getFrequency(): Int? = withContext(Dispatchers.IO){ getWifiInfo().firstOrNull()?.frequency }

    suspend fun getSupplicantState(): SupplicantState? = withContext(Dispatchers.IO) { getWifiInfo().firstOrNull()?.supplicantState }

    suspend fun isHiddenSSID(): Boolean? = withContext(Dispatchers.IO) { getWifiInfo().firstOrNull()?.hiddenSSID }

}