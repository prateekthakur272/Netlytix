package dev.prateekthakur.netlytix.domain.repository

import dev.prateekthakur.netlytix.domain.models.InternetLatency
import kotlinx.coroutines.flow.Flow

interface InternetConnectionRepository {
    suspend fun ping(): InternetLatency
    fun latency(): Flow<InternetLatency>
    suspend fun getPublicIpV4Address(): String?
    suspend fun getPublicIpV6Address(): String?
}