package ru.kima.intelligentchat.domain.messaging.generation.strategies

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationRequest
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStatus
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStrategy

class KoboldAiGenerationStrategy : GenerationStrategy {
    override fun generate(request: GenerationRequest): Flow<GenerationStatus> {
        TODO("Not yet implemented")
    }


    override suspend fun cancelGeneration(requestId: String) {
        TODO("Not yet implemented")
    }
}