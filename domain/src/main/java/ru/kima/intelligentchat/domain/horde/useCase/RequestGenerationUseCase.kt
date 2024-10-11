package ru.kima.intelligentchat.domain.horde.useCase

import ru.kima.intelligentchat.core.common.ICResult
import ru.kima.intelligentchat.domain.horde.model.GenerationInput
import ru.kima.intelligentchat.domain.horde.model.HordeAsyncRequest
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository

class RequestGenerationUseCase(
    private val hordeRepository: HordeRepository
) {
    suspend operator fun invoke(
        apiKey: String,
        generationInput: GenerationInput
    ) {
        return when (hordeRepository.requestGeneration(apiKey, generationInput)) {
            is ICResult.Error -> TODO()
            is ICResult.Success -> TODO()
        }
    }

    sealed interface Result {
        data class Success(val hordeRequest: HordeAsyncRequest) : Result

    }
}