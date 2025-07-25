package dev.prateekthakur.netlytix.ui.features.networkInfo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.prateekthakur.netlytix.domain.models.WifiConnectionInfo
import dev.prateekthakur.netlytix.domain.repository.WifiConnectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WifiConnectionInfoViewModel(repository: WifiConnectionRepository) : ViewModel() {

    private val mutableState = MutableStateFlow(WifiConnectionInfo())
    val state = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getInfo().collect{
                mutableState.value = it
            }
        }
    }
}