package ru.kima.intelligentchat.domain.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler
import ru.kima.intelligentchat.domain.horde.model.ActiveModel
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository

class LoadHordeModelsUseCase(
    private val horde: HordeRepository,
    private val hordeState: HordeStateHandler
) {
    suspend operator fun invoke(): LoadHordeModelsResult {
        val modelsResource = horde.activeModels()
        val models = modelsResource.data
            ?: return errorResultFromMessage(modelsResource.message)

        val workersResource = horde.workers()
        val workers = workersResource.data
            ?: return errorResultFromMessage(workersResource.message)


        val modelsMap = models.map { it.name to it }

        workers.forEach { worker ->
            worker.models.forEach {

            }
        }



        return LoadHordeModelsResult.UnknownError("")
    }

    private fun errorResultFromMessage(message: String?): LoadHordeModelsResult {
        return when (message) {
            "0" -> LoadHordeModelsResult.NoInternet
            else -> LoadHordeModelsResult.UnknownError(
                message ?: "Very unknown message"
            )
        }
    }

    sealed interface LoadHordeModelsResult {
        data class Success(val models: List<ActiveModel>) : LoadHordeModelsResult
        data class UnknownError(val message: String) : LoadHordeModelsResult
        data object NoInternet : LoadHordeModelsResult
    }
}