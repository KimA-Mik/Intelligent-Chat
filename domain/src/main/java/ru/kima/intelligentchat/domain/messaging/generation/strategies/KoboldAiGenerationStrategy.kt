package ru.kima.intelligentchat.domain.messaging.generation.strategies

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ru.kima.intelligentchat.domain.common.errors.GenerationError
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationRequest
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStatus
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStrategy

class KoboldAiGenerationStrategy : GenerationStrategy {
    override fun generate(request: GenerationRequest): Flow<GenerationStatus> =
        flowOf(GenerationStatus.Error(GenerationError.NotImplemented))


    override suspend fun cancelGeneration(requestId: String): Boolean {
        return true
    }
}