package ru.kima.intelligentchat.domain.chat.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.model.ChatWithMessages
import ru.kima.intelligentchat.domain.chat.model.FullChat

interface ChatRepository {
    fun subscribeToChat(chatId: Long): Flow<Chat>
    fun subscribeToCardChats(cardId: Long): Flow<List<ChatWithMessages>>
    suspend fun deleteChat(chat: FullChat)
    suspend fun insertChat(chat: Chat): Long
    suspend fun updateChat(chat: Chat)
}