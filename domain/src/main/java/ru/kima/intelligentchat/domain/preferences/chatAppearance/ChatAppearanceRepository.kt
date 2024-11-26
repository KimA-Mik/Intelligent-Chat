package ru.kima.intelligentchat.domain.preferences.chatAppearance

import kotlinx.coroutines.flow.Flow

interface ChatAppearanceRepository {
    fun chatAppearance(): Flow<ChatAppearance>
    suspend fun updateShowDate(value: Boolean)
    suspend fun updateShowNumber(value: Boolean)
}