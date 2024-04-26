package ru.kima.intelligentchat.domain.chat.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.model.FullChat

interface ChatRepository {
    fun subscribeToChat(chatId: Long): Flow<Chat>
    fun subscribeToCardChat(cardId: Long): Flow<FullChat>
    suspend fun deleteChat(chatId: Long): Boolean
    suspend fun insertChat(chat: Chat): Long
}