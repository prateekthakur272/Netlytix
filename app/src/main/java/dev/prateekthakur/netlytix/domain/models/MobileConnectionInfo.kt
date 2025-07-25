package dev.prateekthakur.netlytix.domain.models

import dev.prateekthakur.netlytix.platform.networkInfo.enums.CellularNetworkType

data class MobileConnectionInfo(
    val carrierName: String? = null,
    val mobileCarrierName: String? = null,
    val networkOperatorName: String? = null,
    val simOperatorName: String? = null,
    val simCountryIso: String? = null,
    val networkCountryIos: String? = null,
    val roaming: Boolean = false,
    val phoneType: CellularNetworkType = CellularNetworkType.UNKNOWN,
    val dataEnabled: Boolean = false
)