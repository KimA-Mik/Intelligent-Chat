package ru.kima.intelligentchat.core.preferences

import android.content.Context
import ru.kima.intelligentchat.core.common.API_TYPE

class PreferencesHandler(context: Context) {
    private val store = context.preferencesDataStore
    val data = store.data

    suspend fun updateSelectedPersona(id: Long) {
        updateData {
            it.copy(
                selectedPersonaId = id
            )
        }
    }

    suspend fun updateSelectedAip(apiType: API_TYPE) {
        updateData {
            it.copy(
                selectedApiType = apiType
            )
        }
    }

    private suspend fun updateData(
        transform: suspend (AppPreferences) -> AppPreferences
    ): AppPreferences {
        return store.updateData(transform)
    }
}