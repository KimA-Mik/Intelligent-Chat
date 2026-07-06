package ru.kima.intelligentchat.domain.messaging.generation.strategies

import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class Retrial(
    private val retryCount: Int = 3,
    private val retryDelay: Duration = 2.seconds
) {
    private var errorCount = 0
    fun clear() {
        errorCount = 0
    }

    suspend fun retry() {
        if (errorCount < 1) return
        delay(retryDelay * errorCount)
    }

    fun incrementErrorCount() {
        errorCount++
    }

    fun shouldEndRetrial(): Boolean {
        return errorCount >= retryCount
    }
}