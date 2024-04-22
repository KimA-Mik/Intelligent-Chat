package ru.kima.intelligentchat.data.chat.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.chat.entities.MessageEntity
import ru.kima.intelligentchat.data.chat.entities.SwipeEntity
import ru.kima.intelligentchat.data.chat.mappers.toChat
import ru.kima.intelligentchat.data.chat.mappers.toMessage
import ru.kima.intelligentchat.data.chat.mappers.toSwipe
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.model.Message
import ru.kima.intelligentchat.domain.chat.model.Swipe
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository

class ChatRepositoryImpl(
    wrapper: DatabaseWrapper,
) : ChatRepository {
    private val chatDao = wrapper.database.chatDao()
    private val messageDao = wrapper.database.messageDao()
    private val swipeDao = wrapper.database.swipeDao()

    override fun subscribeToChat(chatId: Long): Flow<Chat?> {
        return chatDao
            .subscribeToChat(chatId)
            .map { it?.toChat() }
    }

    override fun subscribeToChatMessages(chatId: Long): Flow<List<Message>> {
        return messageDao
            .subscribeToChatMessages(chatId)
            .map { it.map(MessageEntity::toMessage) }
    }

    override fun subscribeSwipesForMessages(messagesId: List<Long>): Flow<List<Swipe>> {
        return swipeDao
            .selectSwipesForMessages(messagesId)
            .map { it.map(SwipeEntity::toSwipe) }
    }
}