package dev.prateekthakur.netlytix.domain.repository

import dev.prateekthakur.netlytix.domain.models.ConnectedNetworkType

interface ConnectionRepository {
    suspend fun getConnectionType(): ConnectedNetworkType
    suspend fun getWifiSSID(): String?
    suspend fun getWifiBSSID(): String?
    suspend fun getIpv4Address(): String?
    suspend fun getIpv6Address(): String?
    suspend fun getGatewayAddress(): String?
    suspend fun getDnsServers(): List<String>
    suspend fun getMacAddress(): String?

    suspend fun getSignalStrength(): Int?
    suspend fun getLinkSpeedMbps(): Int?
    suspend fun getUploadSpeedKbps(): Double
    suspend fun getDownloadSpeedKbps(): Double
    suspend fun getPingLatencyMs(host: String = "google.com"): Long
    suspend fun getPacketLossPercent(host: String = "google.com", pingCount: Int = 5): Double

    suspend fun isInternetAccessible(): Boolean
    suspend fun isVpnActive(): Boolean
    suspend fun getVpnType(): String?
    suspend fun getPublicIpAddress(): String
}