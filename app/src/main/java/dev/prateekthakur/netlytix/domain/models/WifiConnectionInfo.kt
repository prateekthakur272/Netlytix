package dev.prateekthakur.netlytix.domain.models

import android.net.wifi.SupplicantState


data class WifiConnectionInfo(
    val ssid : String? = null,
    val bssid : String? = null,
    val isSSidHidden : Boolean = false,
    val linkSpeed : Int = -1,
    val signalStrength: Int = -1,
    val gatewayAddress: String? = null,
    val ipAddress: String? = null,
    val networkId: Int = -1,
    val frequency: Int = -1,
    val supplicantState: SupplicantState? = null,
)