package ru.kima.intelligentchat.domain.preferences.app

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.common.ApiType
import ru.kima.intelligentchat.domain.preferences.Preference

interface AppPreferencesRepository {
    fun preferences(): Flow<AppPreferences>
    fun showImages(): Preference<Boolean>
    suspend fun updateSelectedPersona(id: Long)
    suspend fun updateSelectedAip(apiType: ApiType)
    suspend fun updateGenerationPending(generationPending: Boolean)

}