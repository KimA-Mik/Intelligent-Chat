package ru.kima.intelligentchat.domain.preferences.textCompletion

import kotlinx.coroutines.flow.Flow

interface TextCompletionPreferencesRepository {
    fun preferences(): Flow<TextCompletionPreferences>
    suspend fun updateBaseUrl(baseUrl: String)
    suspend fun selectPreset(presetId: Long)
}
