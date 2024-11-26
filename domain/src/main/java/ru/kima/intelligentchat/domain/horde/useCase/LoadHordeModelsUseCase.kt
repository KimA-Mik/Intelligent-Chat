package ru.kima.intelligentchat.domain.horde.useCase

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import ru.kima.intelligentchat.domain.horde.mapper.toHordeModelInfo
import ru.kima.intelligentchat.domain.horde.model.HordeWorker
import ru.kima.intelligentchat.domain.horde.model.ModelInfo
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository
import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository

class LoadHordeModelsUseCase(
    private val horde: HordeRepository,
    private val stateRepository: HordeStateRepository,
) {
    suspend operator fun invoke(): LoadHordeModelsResult = coroutineScope {
        val modelsDeferred = async { horde.activeModels() }
        val workersDeferred = async { horde.workers() }

        val modelsResource = modelsDeferred.await()
        val models = modelsResource.data
            ?: return@coroutineScope errorResultFromMessage(modelsResource.message)

        val workersResource = workersDeferred.await()
        val workers = workersResource.data
            ?: return@coroutineScope errorResultFromMessage(workersResource.message)

        val modelsMap = models
            .associateBy({ it.name }, { ModelInfo.fromActiveModel(it) })
            .toMutableMap()

        workers.forEach { worker ->
            handleWorker(worker, modelsMap)
        }

        stateRepository.updateHordeModelInfo(
            modelsMap.mapValues {
                it.value.toHordeModelInfo()
            }
        )

        return@coroutineScope LoadHordeModelsResult.Success(modelsMap.size)
    }

    private fun handleWorker(worker: HordeWorker, modelsMap: MutableMap<String, ModelInfo>) {
        worker.models.forEach { modelName ->
            val model = modelsMap[modelName] ?: return@forEach
            if (worker.maxContextSize < model.maxContextSize
                || worker.maxLength < model.maxResponseLength
            ) {
                modelsMap[modelName] = model.copy(
                    maxContextSize = worker.maxContextSize,
                    maxResponseLength = worker.maxLength
                )
            }
        }
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
        data class Success(val modelsCount: Int) : LoadHordeModelsResult
        data class UnknownError(val message: String) : LoadHordeModelsResult
        data object NoInternet : LoadHordeModelsResult
    }
}