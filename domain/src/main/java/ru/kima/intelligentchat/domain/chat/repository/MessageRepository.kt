package ru.kima.intelligentchat.domain.chat.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.Message
import ru.kima.intelligentchat.domain.chat.model.MessageWithSwipes
import ru.kima.intelligentchat.domain.chat.model.SenderType

interface MessageRepository {
    fun subscribeToChatMessages(chatId: Long): Flow<List<Message>>
    fun subscribeToChatMessagesWithSwipes(chatId: Long): Flow<List<MessageWithSwipes>>
    suspend fun getFullMessage(id: Long): MessageWithSwipes?
    suspend fun getMessage(id: Long): Message?
    suspend fun updateMessage(message: Message)
    suspend fun updateMessages(messages: List<Message>)
    suspend fun getMarkedMessages(): List<MessageWithSwipes>
    suspend fun deleteMessages(messages: List<MessageWithSwipes>)
    suspend fun createMessage(
        chatId: Long,
        sender: SenderType,
        senderId: Long,
        index: Int,
        selectedSwipeIndex: Int
    ): Long
}