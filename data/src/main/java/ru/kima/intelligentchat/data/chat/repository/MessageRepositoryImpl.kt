package ru.kima.intelligentchat.data.chat.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.chat.entities.MessageEntity
import ru.kima.intelligentchat.data.chat.mappers.toMessage
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.domain.chat.model.Message
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository

class MessageRepositoryImpl(
    wrapper: DatabaseWrapper
) : MessageRepository {
    private val messageDao = wrapper.database.messageDao()

    override fun subscribeToChatMessages(chatId: Long): Flow<List<Message>> {
        return messageDao
            .subscribeToChatMessages(chatId)
            .map { it.map(MessageEntity::toMessage) }
    }
}