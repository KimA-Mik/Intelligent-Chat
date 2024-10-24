package ru.kima.intelligentchat.domain.messaging.generation.model

import kotlinx.coroutines.flow.Flow

interface GenerationStrategy {
    fun generation(request: GenerationRequest): Flow<GenerationStatus>
    suspend fun cancelGeneration(requestId: String): Boolean
}