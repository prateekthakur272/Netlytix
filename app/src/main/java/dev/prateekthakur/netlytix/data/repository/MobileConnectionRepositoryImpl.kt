package dev.prateekthakur.netlytix.data.repository

import android.content.Context
import android.telephony.TelephonyManager
import dev.prateekthakur.netlytix.domain.repository.MobileNetworkConnectionRepository

class MobileConnectionRepositoryImpl(context: Context): MobileNetworkConnectionRepository {

    private val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    override fun getInfo() = telephonyManager
}