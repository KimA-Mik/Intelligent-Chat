package ru.kima.intelligentchat.domain.preferences.chatSettings

import kotlinx.coroutines.flow.Flow

interface ChatSettingsRepository {
    fun chatSettings(): Flow<ChatSettings>
    suspend fun updateShowDate(value: Boolean)
    suspend fun updateShowNumber(value: Boolean)
}