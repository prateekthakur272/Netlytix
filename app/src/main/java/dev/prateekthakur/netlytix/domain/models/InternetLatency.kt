package dev.prateekthakur.netlytix.domain.models

data class InternetLatency(
    val latency: Long,
    val packetLoss: Double
)
