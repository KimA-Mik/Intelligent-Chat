package ru.kima.intelligentchat.domain.messaging.strategies

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.messaging.model.GenerationStatus
import ru.kima.intelligentchat.domain.messaging.model.GenerationStrategy

class KoboldAiGenerationStrategy : GenerationStrategy {
    override fun generate(
        generationInput: String,
        stopSequence: List<String>
    ): Flow<GenerationStatus> {
        TODO("Not yet implemented")
    }


    override suspend fun cancelGeneration(requestId: String) {
        TODO("Not yet implemented")
    }
}