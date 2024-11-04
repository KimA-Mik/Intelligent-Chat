package ru.kima.intelligentchat.data.chat.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.chat.entities.MessageEntity
import ru.kima.intelligentchat.data.chat.mappers.toDto
import ru.kima.intelligentchat.data.chat.mappers.toMessage
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.domain.chat.model.Message
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository

class MessageRepositoryImpl(
    wrapper: DatabaseWrapper
) : MessageRepository {
    private val messageDao = wrapper.database.messageDao()

    override fun subscribeToChatMessages(chatId: Long): Flow<List<Message>> {
        return messageDao
            .chatMessages(chatId)
            .map { it.map(MessageEntity::toMessage) }
    }

    override suspend fun updateMessage(message: Message) {
        messageDao.updateMassage(message.toMessage())
    }

    override suspend fun updateMessages(messages: List<Message>) {
        messageDao.updateMessages(
            messages.map { it.toMessage() }
        )
    }

    override suspend fun deleteMessage(messageId: Long): Boolean {
        return messageDao.deleteMessage(messageId) > 0
    }

    override suspend fun deleteMessages(messageIds: List<Long>): Boolean {
        return messageDao.deleteMessages(messageIds) > 0
    }

    override suspend fun deleteMessagesChat(chatId: Long): Boolean {
        return messageDao.deleteMessagesForChat(chatId) > 0
    }

    override suspend fun createMessage(
        chatId: Long,
        sender: SenderType,
        senderId: Long,
        index: Int,
        selectedSwipeIndex: Int
    ): Long {
        val message = MessageEntity(
            messageId = 0,
            chatId = chatId,
            sender = sender.toDto(),
            senderId = senderId,
            index = index,
            selectedSwipeIndex = selectedSwipeIndex,
            deleted = false
        )

        return messageDao.insertMessage(message)
    }
}