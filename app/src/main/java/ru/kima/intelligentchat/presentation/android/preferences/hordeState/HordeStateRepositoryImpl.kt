package ru.kima.intelligentchat.presentation.android.preferences.hordeState

import android.content.Context
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository
import ru.kima.intelligentchat.domain.preferences.horde.model.HordeModelInfo
import ru.kima.intelligentchat.presentation.android.preferences.hordeState.mapper.toHordeState
import ru.kima.intelligentchat.presentation.android.preferences.hordeState.mapper.toSchema

class HordeStateRepositoryImpl(context: Context) : HordeStateRepository {
    private val store = context.hordeStateDataStore
    val data = store.data
    override fun hordeState() = store.data.map {
        it.toHordeState()
    }

    override suspend fun selectPreset(presetId: Long) {
        updateData {
            it.copy(
                selectedPreset = presetId
            )
        }
    }

    override suspend fun updateHordeModelInfo(modelsInfo: Map<String, HordeModelInfo>) {
        updateData { data ->
            data.copy(
                modelsInfo = modelsInfo.mapValues { it.value.toSchema() }
            )
        }
    }

    override suspend fun updateActualGenerationDetails(
        contextSize: Int,
        responseLength: Int
    ) {
        updateData {
            it.copy(
                actualContextSize = if (contextSize > 0) contextSize else it.actualContextSize,
                actualResponseLength = if (responseLength > 0) responseLength else it.actualResponseLength
            )
        }
    }

    override suspend fun updateUserGenerationDetails(
        contextSize: Int,
        responseLength: Int
    ) {
        updateData {
            it.copy(
                userContextSize = if (contextSize > 0) contextSize else it.userContextSize,
                userResponseLength = if (responseLength > 0) responseLength else it.userResponseLength
            )
        }
    }

    override suspend fun selectModels(models: List<String>) {
        updateData {
            it.copy(
                selectedModels = models
            )
        }
    }

    override suspend fun updateContextToWorker(contextToWorker: Boolean) {
        updateData {
            it.copy(
                contextToWorker = contextToWorker
            )
        }
    }

    override suspend fun updateResponseToWorker(responseToWorker: Boolean) {
        updateData {
            it.copy(
                responseToWorker = responseToWorker
            )
        }
    }

    override suspend fun updateTrustedWorkers(trustedWorkers: Boolean) {
        updateData {
            it.copy(
                trustedWorkers = trustedWorkers
            )
        }
    }

    override suspend fun updateUserData(apiToken: String, userName: String, userId: Int) {
        updateData {
            it.copy(
                apiToken = apiToken,
                userName = userName,
                userId = userId
            )
        }
    }

    override suspend fun updateGenerationId(generationId: String?) {
        updateData {
            it.copy(
                generationId = generationId
            )
        }
    }

    private suspend fun updateData(
        transform: suspend (HordeStateSchema) -> HordeStateSchema
    ): HordeStateSchema {
        return store.updateData(transform)
    }
}