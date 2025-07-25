package dev.prateekthakur.netlytix.platform.networkInfo.enums

enum class VpnConnectionType (val description: String) {
    TUN_BASED("TUN-based (likely WireGuard or OpenVpn)"),
    PPP_BASED("PPP-based (L2TP)"),
    IPSEC("IPSec-based (IKEv2 / L2TP/IPSec)"),
    UNKNOWN("Unknown VPN type"),
    NONE("No VPN connection")
}