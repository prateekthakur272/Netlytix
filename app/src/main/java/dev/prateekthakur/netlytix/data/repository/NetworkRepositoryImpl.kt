package dev.prateekthakur.netlytix.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.util.Log
import dev.prateekthakur.netlytix.domain.models.ConnectedNetworkType
import dev.prateekthakur.netlytix.domain.repository.ConnectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.URL
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(context: Context) : ConnectionRepository {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val wifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager


    override suspend fun getConnectionType(): ConnectedNetworkType {
        val network = connectivityManager.activeNetwork ?: return ConnectedNetworkType.NONE
        val capabilities = connectivityManager.getNetworkCapabilities(network)
            ?: return ConnectedNetworkType.UNKNOWN

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectedNetworkType.WIFI
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectedNetworkType.DATA
            else -> ConnectedNetworkType.OTHER
        }
    }

    override suspend fun getWifiSSID(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val info = wifiManager.connectionInfo
                val ssid = info.ssid
                if (ssid != null && ssid != WifiManager.UNKNOWN_SSID) ssid.trim('"') else null
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Error getting SSID", e)
                null
            }
        }
    }

    override suspend fun getWifiBSSID(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val info = wifiManager.connectionInfo
                val bssid = info.bssid
                if (!bssid.isNullOrBlank()) bssid else null
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Error getting BSSID", e)
                null
            }
        }
    }

    override suspend fun getIpv4Address(): String? {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                NetworkInterface.getNetworkInterfaces().toList().asSequence()
                    .flatMap { it.inetAddresses.toList().asSequence() }.firstOrNull { address ->
                        !address.isLoopbackAddress && address is Inet4Address
                    }?.hostAddress
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Error getting IPv4 address", e)
                null
            }
        }
    }

    override suspend fun getIpv6Address(): String? {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                NetworkInterface.getNetworkInterfaces().toList().asSequence()
                    .flatMap { it.inetAddresses.toList().asSequence() }.firstOrNull { address ->
                        !address.isLoopbackAddress && address is Inet6Address && address.hostAddress?.contains(
                            "%"
                        ) == true
                    }?.hostAddress
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Error getting IPv6 address", e)
                null
            }
        }
    }

    override suspend fun getGatewayAddress(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val dhcpInfo = wifiManager.dhcpInfo
                val gateway = dhcpInfo.gateway
                if (gateway != 0) {
                    val ip = InetAddress.getByAddress(
                        byteArrayOf(
                            (gateway and 0xFF).toByte(),
                            (gateway shr 8 and 0xFF).toByte(),
                            (gateway shr 16 and 0xFF).toByte(),
                            (gateway shr 24 and 0xFF).toByte()
                        )
                    ).hostAddress
                    ip
                } else null
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Error getting Gateway", e)
                null
            }
        }
    }

    override suspend fun getDnsServers(): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                val network = connectivityManager.activeNetwork ?: return@withContext emptyList()
                val linkProperties =
                    connectivityManager.getLinkProperties(network) ?: return@withContext emptyList()
                linkProperties.dnsServers.map { it.hostAddress ?: "" }.filter { it.isNotEmpty() }
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Error getting DNS servers", e)
                emptyList()
            }
        }
    }

    override suspend fun getMacAddress(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val interfaces = NetworkInterface.getNetworkInterfaces()
                for (networkInterface in interfaces) {
                    if (networkInterface.name.equals("wlan0", ignoreCase = true)) {
                        val macBytes = networkInterface.hardwareAddress ?: return@withContext null
                        return@withContext macBytes.joinToString(":") { "%02X".format(it) }
                    }
                }
                null
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Error getting MAC address", e)
                null
            }
        }
    }

    override suspend fun getSignalStrength(): Int? {
        return withContext(Dispatchers.IO) {
            try {
                return@withContext wifiManager.connectionInfo?.rssi
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Error getting signal strength", e)
                null
            }
        }
    }

    override suspend fun getLinkSpeedMbps(): Int? {
        return withContext(Dispatchers.IO) {
            try {
                return@withContext wifiManager.connectionInfo?.linkSpeed
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Error getting link speed", e)
                null
            }
        }
    }

    override suspend fun getUploadSpeedKbps(): Double {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://httpbin.org/post") // echo server
                val connection = url.openConnection() as HttpURLConnection
                connection.doOutput = true
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/octet-stream")

                val data = ByteArray(2 * 1024 * 1024)
                val startTime = System.currentTimeMillis()

                connection.outputStream.use { it.write(data) }

                val duration = System.currentTimeMillis() - startTime
                val kbps = if (duration > 0) (data.size * 8) / duration.toDouble() else 0.0

                connection.inputStream.close()
                connection.disconnect()
                kbps
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Upload speed error", e)
                0.0
            }
        }
    }

    override suspend fun getDownloadSpeedKbps(): Double {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://speed.hetzner.de/100MB.bin") // reliable test file
                val connection = url.openConnection()
                connection.connect()

                val inputStream = connection.getInputStream()
                val buffer = ByteArray(1024 * 8) // 8KB buffer
                var bytesRead: Int
                var totalBytes = 0L
                val startTime = System.currentTimeMillis()

                while (inputStream.read(buffer)
                        .also { bytesRead = it } != -1 && totalBytes < 5 * 1024 * 1024
                ) {
                    totalBytes += bytesRead
                }

                val duration = System.currentTimeMillis() - startTime
                val kbps =
                    if (duration > 0) (totalBytes * 8) / duration.toDouble() else 0.0 // in Kbps

                inputStream.close()
                kbps
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Download speed error", e)
                0.0
            }
        }
    }

    override suspend fun getPingLatencyMs(host: String): Long {
        return withContext(Dispatchers.IO) {
            try {
                val process = Runtime.getRuntime().exec("ping -c 1 $host")
                val reader = process.inputStream.bufferedReader()
                val output = reader.readText()
                process.waitFor()

                // Example line: "64 bytes from ... time=23.456 ms"
                val timeRegex = "time=([0-9.]+)".toRegex()
                val match = timeRegex.find(output)
                match?.groups?.get(1)?.value?.toDouble()?.toLong() ?: -1L
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Ping failed", e)
                -1L
            }
        }
    }

    override suspend fun getPacketLossPercent(host: String, pingCount: Int): Double {
        return withContext(Dispatchers.IO) {
            try {
                val process = Runtime.getRuntime().exec("ping -c $pingCount $host")
                val reader = process.inputStream.bufferedReader()
                val output = reader.readText()
                process.waitFor()

                // Example line: "5 packets transmitted, 5 received, 0% packet loss"
                val lossRegex = "(\\d+)% packet loss".toRegex()
                val match = lossRegex.find(output)
                match?.groups?.get(1)?.value?.toDouble() ?: -1.0
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Packet loss check failed", e)
                -1.0
            }
        }
    }

    override suspend fun isInternetAccessible(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val address = InetAddress.getByName("google.com")
                !address.hostAddress.isNullOrEmpty()
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Internet check failed", e)
                false
            }
        }
    }

    override suspend fun isVpnActive(): Boolean {
        return !getVpnType().isNullOrBlank()
    }

    override suspend fun getVpnType(): String? {
        return withContext(Dispatchers.IO) {
            try {
                NetworkInterface.getNetworkInterfaces().toList().firstOrNull { iface ->
                    iface.isUp && (iface.name.startsWith("tun") || iface.name.startsWith("ppp") || iface.name.startsWith(
                        "ipsec"
                    ))
                }?.let { networkInterface ->
                    when {
                        networkInterface.name.startsWith("tun") -> "TUN (Typical VPN)"
                        networkInterface.name.startsWith("ppp") -> "PPP (Point-to-Point)"
                        networkInterface.name.startsWith("ipsec") -> "IPSec"
                        else -> "Unknown"
                    }
                }
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "VPN type detection failed", e)
                null
            }
        }
    }

    override suspend fun getPublicIpAddress(): String {
        return withContext(Dispatchers.IO) {
            try {
                URL("https://api.ipify.org").readText()
            } catch (e: Exception) {
                Log.e("NetworkRepositoryImpl", "Error fetching public IP", e)
                "Unavailable"
            }
        }
    }
}