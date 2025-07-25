package dev.prateekthakur.netlytix.domain.repository

import dev.prateekthakur.netlytix.domain.models.NetworkConnectionInfo
import kotlinx.coroutines.flow.Flow

interface NetworkConnectionRepository {
    fun getInfo() : Flow<NetworkConnectionInfo?>
}