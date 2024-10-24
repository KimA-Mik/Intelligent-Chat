package ru.kima.intelligentchat.domain.chat.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.Message
import ru.kima.intelligentchat.domain.chat.model.SenderType

interface MessageRepository {
    fun subscribeToChatMessages(chatId: Long): Flow<List<Message>>
    suspend fun deleteMessage(messageId: Long): Boolean
    suspend fun deleteMessages(messageIds: List<Long>): Boolean
    suspend fun deleteMessagesChat(chatId: Long): Boolean
    suspend fun createMessage(
        chatId: Long,
        sender: SenderType,
        senderId: Long,
        index: Int,
        selectedSwipeIndex: Int
    ): Long
}