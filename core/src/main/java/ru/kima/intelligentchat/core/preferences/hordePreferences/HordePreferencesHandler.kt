package ru.kima.intelligentchat.core.preferences.hordePreferences

import android.content.Context

class HordePreferencesHandler(context: Context) {
    private val store = context.hordePreferencesDataStore
    val data = store.data

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
        transform: suspend (HordePreferences) -> HordePreferences
    ): HordePreferences {
        return store.updateData(transform)
    }
}