package dev.prateekthakur.netlytix.ui.features.networkInfo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.prateekthakur.netlytix.domain.models.NetworkConnectionInfo
import dev.prateekthakur.netlytix.domain.repository.NetworkConnectionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NetworkInfoViewModel(repository: NetworkConnectionRepository) : ViewModel() {

    private val mutableState = MutableStateFlow(NetworkConnectionInfo())
    val state = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getInfo().collect{
                mutableState.value = it
            }
        }
    }
}