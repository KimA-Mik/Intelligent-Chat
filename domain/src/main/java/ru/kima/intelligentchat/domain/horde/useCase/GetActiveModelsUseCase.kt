package ru.kima.intelligentchat.domain.horde.useCase

import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.horde.model.ActiveModel
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository

class GetActiveModelsUseCase(private val repository: HordeRepository) {
    suspend operator fun invoke(): GetActiveModelsResult {
        return when (val resource = repository.activeModels()) {
            is Resource.Success -> GetActiveModelsResult.Success(resource.data!!)
            else -> when (resource.message) {
                "0" -> GetActiveModelsResult.NoInternet
                else -> GetActiveModelsResult.UnknownError(
                    resource.message ?: "Very unknown message"
                )
            }
        }
    }

    sealed interface GetActiveModelsResult {
        data class Success(val models: List<ActiveModel>) : GetActiveModelsResult
        data class UnknownError(val message: String) : GetActiveModelsResult
        data object NoInternet : GetActiveModelsResult
    }
}