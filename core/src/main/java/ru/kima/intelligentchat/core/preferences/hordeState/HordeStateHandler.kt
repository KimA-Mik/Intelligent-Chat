package ru.kima.intelligentchat.core.preferences.hordeState

import android.content.Context

class HordeStateHandler(context: Context) {
    private val store = context.hordeStateDataStore
    val data = store.data

    suspend fun updateGenerationDetails(
        contextSize: Int = -1,
        responseLength: Int = -1
    ) {
        updateData {
            it.copy(
                contextSize = if (contextSize > 0) contextSize else it.contextSize,
                responseLength = if (responseLength > 0) responseLength else it.responseLength
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

    private suspend fun updateData(
        transform: suspend (HordeState) -> HordeState
    ): HordeState {
        return store.updateData(transform)
    }
}