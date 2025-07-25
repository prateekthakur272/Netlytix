package dev.prateekthakur.netlytix.domain.repository

import dev.prateekthakur.netlytix.domain.models.WifiConnectionInfo
import kotlinx.coroutines.flow.Flow

interface WifiConnectionRepository {
    fun getInfo() : Flow<WifiConnectionInfo>
}