package ru.kima.intelligentchat.core.preferences.hordePreferences

import android.content.Context

class HordePreferencesHandler(context: Context) {
    private val store = context.hordePreferencesDataStore
    val data = store.data

    suspend fun updateApiKey(token: String) {
        updateData {
            it.copy(
                apiToken = token
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

    private suspend fun updateData(
        transform: suspend (HordePreferences) -> HordePreferences
    ): HordePreferences {
        return store.updateData(transform)
    }
}