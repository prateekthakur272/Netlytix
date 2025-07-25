package dev.prateekthakur.netlytix.platform.networkInfo.services

import android.Manifest
import android.content.Context
import android.telephony.CellInfo
import android.telephony.TelephonyManager
import androidx.annotation.RequiresPermission
import dev.prateekthakur.netlytix.platform.networkInfo.enums.CellularNetworkType

class MobileConnectionPlatformService(private val context: Context) {

    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun getAllCellInfo(): List<CellInfo>? { return telephonyManager.allCellInfo }

    fun getMobileCarrierName(context: Context): String? = telephonyManager.networkOperatorName.takeIf { it.isNotBlank() }

    fun getNetworkOperator(): String? = telephonyManager.networkOperator.takeIf { it.isNotBlank() }

    fun getSimOperatorName(): String? = telephonyManager.simOperatorName.takeIf { it.isNotBlank() }

    fun getSimCountryIso(): String? = telephonyManager.simCountryIso.takeIf { it.isNotBlank() }

    fun getNetworkCountryIso(): String? = telephonyManager.networkCountryIso.takeIf { it.isNotBlank() }

    fun isNetworkRoaming(): Boolean = telephonyManager.isNetworkRoaming

    fun getPhoneType(): CellularNetworkType {
        return when (telephonyManager.phoneType) {
            TelephonyManager.PHONE_TYPE_GSM -> CellularNetworkType.GSM
            TelephonyManager.PHONE_TYPE_CDMA -> CellularNetworkType.CDMA
            TelephonyManager.PHONE_TYPE_SIP -> CellularNetworkType.SIP
            TelephonyManager.PHONE_TYPE_NONE -> CellularNetworkType.NONE
            else -> CellularNetworkType.UNKNOWN
        }
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    fun isMobileDataEnabled(): Boolean {
        return telephonyManager.isDataEnabled
    }
}