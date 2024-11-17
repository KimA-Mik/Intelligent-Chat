package ru.kima.intelligentchat.data.chat.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.chat.dto.SimpleChatWithMessagesDto
import ru.kima.intelligentchat.data.chat.mappers.toChat
import ru.kima.intelligentchat.data.chat.mappers.toChatWithMessages
import ru.kima.intelligentchat.data.chat.mappers.toDto
import ru.kima.intelligentchat.data.chat.mappers.toEntity
import ru.kima.intelligentchat.data.chat.mappers.toFullChat
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.domain.chat.ChatNotFoundException
import ru.kima.intelligentchat.domain.chat.model.Chat
import ru.kima.intelligentchat.domain.chat.model.ChatWithMessages
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository

class ChatRepositoryImpl(
    wrapper: DatabaseWrapper,
) : ChatRepository {
    private val chatDao = wrapper.database.chatDao()

    override fun subscribeToChat(chatId: Long): Flow<Chat> {
        return chatDao
            .subscribeToChat(chatId)
            .map {
                if (it == null) {
                    throw (ChatNotFoundException())
                }
                it.toChat()
            }
    }

    override fun subscribeToCardChats(cardId: Long): Flow<List<ChatWithMessages>> {
        return chatDao.subscribeToCardChats(cardId)
            .map { it.map(SimpleChatWithMessagesDto::toChatWithMessages) }
    }

    override suspend fun getChatsForCardIds(cardIds: List<Long>): List<FullChat> {
        return chatDao.getChatsForCardIds(cardIds).map { it.toFullChat() }
    }

    override suspend fun deleteChat(chat: FullChat) {
        return chatDao.deleteChat(chat.toDto())
    }

    override suspend fun deleteChats(chats: List<FullChat>) {
        chatDao.deleteChatsTransaction(chats.map { it.toDto() })
    }

    override suspend fun insertChat(chat: Chat): Long {
        return chatDao.insertChat(chat.toEntity())
    }

    override suspend fun updateChat(chat: Chat) {
        chatDao.updateChat(chat.toEntity())
    }

    override suspend fun copyChat(fullChat: FullChat): Long {
        return chatDao.copyChat(fullChat.toDto())
    }
}