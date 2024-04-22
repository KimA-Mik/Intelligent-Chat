package ru.kima.intelligentchat.domain.chat.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.model.Message
import ru.kima.intelligentchat.domain.chat.model.Swipe

interface ChatRepository {
    fun subscribeToChat(chatId: Long): Flow<Chat?>
    fun subscribeToChatMessages(chatId: Long): Flow<List<Message>>
    fun subscribeSwipesForMessages(messagesId: List<Long>): Flow<List<Swipe>>
}