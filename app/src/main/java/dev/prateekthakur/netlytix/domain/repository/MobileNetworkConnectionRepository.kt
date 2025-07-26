package dev.prateekthakur.netlytix.domain.repository

import android.telephony.TelephonyManager

interface MobileNetworkConnectionRepository {
    fun getInfo() : TelephonyManager
}