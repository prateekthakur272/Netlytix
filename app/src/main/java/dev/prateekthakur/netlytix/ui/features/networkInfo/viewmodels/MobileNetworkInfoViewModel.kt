package dev.prateekthakur.netlytix.ui.features.networkInfo.viewmodels

import android.telephony.TelephonyManager
import androidx.lifecycle.ViewModel
import dev.prateekthakur.netlytix.domain.repository.MobileNetworkConnectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MobileNetworkInfoViewModel(repository: MobileNetworkConnectionRepository) : ViewModel() {
    private val mutableState = MutableStateFlow<TelephonyManager?>(null)
    val state = mutableState.asStateFlow()

    init {
        mutableState.value = repository.getInfo()
    }
}