package ru.kima.intelligentchat.core.preferences.hordeState

import android.content.Context
import ru.kima.intelligentchat.core.preferences.hordeState.model.HordeModelInfo

class HordeStateHandler(context: Context) {
    private val store = context.hordeStateDataStore
    val data = store.data

    suspend fun selectPreset(presetId: Long) {
        updateData {
            it.copy(
                selectedPreset = presetId
            )
        }
    }

    suspend fun updateHordeModelInfo(modelsInfo: Map<String, HordeModelInfo>) {
        updateData {
            it.copy(
                modelsInfo = modelsInfo
            )
        }
    }

    suspend fun updateActualGenerationDetails(
        contextSize: Int = -1,
        responseLength: Int = -1
    ) {
        updateData {
            it.copy(
                actualContextSize = if (contextSize > 0) contextSize else it.actualContextSize,
                actualResponseLength = if (responseLength > 0) responseLength else it.actualResponseLength
            )
        }
    }

    suspend fun updateUserGenerationDetails(
        contextSize: Int = -1,
        responseLength: Int = -1
    ) {
        updateData {
            it.copy(
                userContextSize = if (contextSize > 0) contextSize else it.userContextSize,
                userResponseLength = if (responseLength > 0) responseLength else it.userResponseLength
            )
        }
    }

    suspend fun selectModels(models: List<String>) {
        updateData {
            it.copy(
                selectedModels = models
            )
        }
    }

    suspend fun updateContextToWorker(contextToWorker: Boolean) {
        updateData {
            it.copy(
                contextToWorker = contextToWorker
            )
        }
    }

    suspend fun updateResponseToWorker(responseToWorker: Boolean) {
        updateData {
            it.copy(
                responseToWorker = responseToWorker
            )
        }
    }

    suspend fun updateTrustedWorkers(trustedWorkers: Boolean) {
        updateData {
            it.copy(
                trustedWorkers = trustedWorkers
            )
        }
    }

    suspend fun updateUserData(apiToken: String, userName: String, userId: Int) {
        updateData {
            it.copy(
                apiToken = apiToken,
                userName = userName,
                userId = userId
            )
        }
    }

    suspend fun updateGenerationId(generationId: String?) {
        updateData {
            it.copy(
                generationId = generationId
            )
        }
    }

    private suspend fun updateData(
        transform: suspend (HordeState) -> HordeState
    ): HordeState {
        return store.updateData(transform)
    }
}