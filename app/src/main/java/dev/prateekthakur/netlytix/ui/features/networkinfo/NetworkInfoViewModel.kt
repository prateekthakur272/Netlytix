package dev.prateekthakur.netlytix.ui.features.networkinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.prateekthakur.netlytix.domain.repository.ConnectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkInfoViewModel @Inject constructor(private val repository: ConnectionRepository) : ViewModel() {

    private val mutableState = MutableStateFlow(NetworkInfoState())
    val state = mutableState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            launch {
                mutableState.update {
                    it.copy(connectionType = repository.getConnectionType())
                }
            }

            launch {
                repository.getWifiSSID()?.let { ssid ->
                    mutableState.update { it.copy(ssid = ssid) }
                }
            }

            launch {
                repository.getWifiBSSID()?.let { bssid ->
                    mutableState.update { it.copy(bssid = bssid) }
                }
            }

            launch {
                repository.getIpv4Address()?.let { ipv4 ->
                    mutableState.update { it.copy(ipv4 = ipv4) }
                }
            }

            launch {
                repository.getIpv6Address()?.let { ipv6 ->
                    mutableState.update { it.copy(ipv6 = ipv6) }
                }
            }

            launch {
                repository.getGatewayAddress()?.let { gateway ->
                    mutableState.update { it.copy(gateway = gateway) }
                }
            }

            launch {
                val dns = repository.getDnsServers()
                if (dns.isNotEmpty()) {
                    mutableState.update { it.copy(dnsServers = dns) }
                }
            }

            launch {
                repository.getMacAddress()?.let { mac ->
                    mutableState.update { it.copy(macAddress = mac) }
                }
            }

            launch {
                repository.getSignalStrength()?.let { strength ->
                    mutableState.update { it.copy(signalStrength = strength) }
                }
            }

            launch {
                repository.getLinkSpeedMbps()?.let { speed ->
                    mutableState.update { it.copy(linkSpeedMbps = speed) }
                }
            }

            launch {
                val ping = repository.getPingLatencyMs()
                if (ping >= 0) {
                    mutableState.update { it.copy(pingLatencyMs = ping) }
                }
            }

            launch {
                val loss = repository.getPacketLossPercent()
                if (loss >= 0) {
                    mutableState.update { it.copy(packetLossPercent = loss) }
                }
            }

            launch {
                val internet = repository.isInternetAccessible()
                mutableState.update { it.copy(isInternetAccessible = internet) }
            }

            launch {
                val vpn = repository.isVpnActive()
                mutableState.update { it.copy(isVpnActive = vpn) }
            }

            launch {
                repository.getVpnType()?.let { type ->
                    mutableState.update { it.copy(vpnType = type) }
                }
            }

            launch {
                val publicIp = repository.getPublicIpAddress()
                if (publicIp.isNotBlank()) {
                    mutableState.update { it.copy(publicIp = publicIp) }
                }
            }

            launch {
                val upload = repository.getUploadSpeedKbps()
                if (upload >= 0) {
                    mutableState.update { it.copy(uploadSpeedKbps = upload) }
                }
            }

            launch {
                val download = repository.getDownloadSpeedKbps()
                if (download >= 0) {
                    mutableState.update { it.copy(downloadSpeedKbps = download) }
                }
            }
        }
    }
}