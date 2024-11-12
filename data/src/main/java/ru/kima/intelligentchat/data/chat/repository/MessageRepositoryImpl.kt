package ru.kima.intelligentchat.data.chat.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.chat.dto.MessageWithSwipesDto
import ru.kima.intelligentchat.data.chat.entities.MessageEntity
import ru.kima.intelligentchat.data.chat.mappers.toDto
import ru.kima.intelligentchat.data.chat.mappers.toEntity
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.domain.chat.model.Message
import ru.kima.intelligentchat.domain.chat.model.MessageWithSwipes
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository

class MessageRepositoryImpl(
    wrapper: DatabaseWrapper
) : MessageRepository {
    private val chatDao = wrapper.database.chatDao()

    override fun subscribeToChatMessagesWithSwipes(chatId: Long): Flow<List<MessageWithSwipes>> {
        return chatDao
            .chatWithMessages(chatId)
            .map { it.map(MessageWithSwipesDto::toEntity) }
    }

    override suspend fun getFullMessage(id: Long): MessageWithSwipes? {
        return chatDao.getFullMessage(id)?.toEntity()
    }

    override fun subscribeToChatMessages(chatId: Long): Flow<List<Message>> {
        return chatDao
            .chatMessages(chatId)
            .map { it.map(MessageEntity::toEntity) }
    }

    override suspend fun updateMessage(message: Message) {
        chatDao.updateMassage(message.toEntity())
    }

    override suspend fun updateMessages(messages: List<Message>) {
        chatDao.updateMessages(
            messages.map { it.toEntity() }
        )
    }

    override suspend fun deleteMessage(messageId: Long): Boolean {
        return chatDao.deleteMessage(messageId) > 0
    }

    override suspend fun deleteMessages(messageIds: List<Message>) {
        chatDao.deleteMessages(messageIds.map { it.toEntity() })
    }

    override suspend fun deleteMessagesChat(chatId: Long): Boolean {
        return chatDao.deleteMessagesForChat(chatId) > 0
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

        return chatDao.insertMessage(message)
    }
}