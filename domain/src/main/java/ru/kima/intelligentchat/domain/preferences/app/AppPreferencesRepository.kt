package ru.kima.intelligentchat.domain.preferences.app

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.common.ApiType

interface AppPreferencesRepository {
    fun preferences(): Flow<AppPreferences>
    suspend fun updateSelectedPersona(id: Long)
    suspend fun updateSelectedAip(apiType: ApiType)
    suspend fun updateGenerationPending(generationPending: Boolean)

}