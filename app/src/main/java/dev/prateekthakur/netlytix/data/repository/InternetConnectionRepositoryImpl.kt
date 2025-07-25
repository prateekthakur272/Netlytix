package dev.prateekthakur.netlytix.data.repository

import dev.prateekthakur.netlytix.domain.models.InternetLatency
import dev.prateekthakur.netlytix.domain.repository.InternetConnectionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.net.URL

class InternetConnectionRepositoryImpl : InternetConnectionRepository {

    override suspend fun ping(): InternetLatency {
        return try {
            val process = withContext(Dispatchers.IO) { ProcessBuilder("ping", "-c", "4", "8.8.8.8").start() }
            val output = process.inputStream.bufferedReader().readText()
            val timeRegex = Regex("time=([0-9.]+)")
            val times = timeRegex.findAll(output).mapNotNull { it.groupValues[1].toDoubleOrNull() }.toList()
            val avgLatency = if (times.isNotEmpty()) times.average().toLong() else -1L
            val lossRegex = Regex("(\\d+)% packet loss")
            val loss = lossRegex.find(output)?.groupValues?.get(1)?.toDoubleOrNull() ?: -1.0
            InternetLatency(avgLatency, loss)
        } catch (e: Exception) {
            InternetLatency(-1, -1.0)
        }
    }

    override fun latency(): Flow<InternetLatency> = flow {
        while (true) {
            val stats = ping()
            emit(stats)
            delay(1000)
        }
    }

    override suspend fun getPublicIpV4Address(): String? = withContext(Dispatchers.IO) {
        return@withContext try {
            URL("https://api.ipify.org").readText().trim()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getPublicIpV6Address(): String? = withContext(Dispatchers.IO) {
        return@withContext try {
            URL("https://api64.ipify.org").readText().trim()
        } catch (e: Exception) {
            null
        }
    }
}