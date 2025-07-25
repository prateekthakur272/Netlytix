package dev.prateekthakur.netlytix.domain.models

import dev.prateekthakur.netlytix.platform.networkInfo.enums.VpnConnectionType

data class VpnConnectionInfo(val type: VpnConnectionType = VpnConnectionType.NONE)