package ru.kima.intelligentchat.domain.messaging.model

import kotlinx.coroutines.flow.Flow

interface GenerationStrategy {
    fun generate(generationInput: String, stopSequence: List<String>): Flow<GenerationStatus>
    suspend fun cancelGeneration(requestId: String)
}