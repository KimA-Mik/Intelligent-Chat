package ru.kima.intelligentchat.domain.chat.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.Chat

interface ChatRepository {
    fun subscribeToChat(chatId: Long): Flow<Chat>
    suspend fun deleteChat(chatId: Long): Boolean
}