package ru.kima.intelligentchat.domain.chat.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.model.ChatWithMessages

interface ChatRepository {
    fun subscribeToChat(chatId: Long): Flow<Chat>
    fun subscribeToCardChats(cardId: Long): Flow<List<ChatWithMessages>>
    suspend fun deleteChat(chatId: Long): Boolean
    suspend fun insertChat(chat: Chat): Long
    suspend fun updateChat(chat: Chat)
}