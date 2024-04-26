package ru.kima.intelligentchat.data.chat.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.data.chat.mappers.toChat
import ru.kima.intelligentchat.data.chat.mappers.toFullChat
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.domain.chat.ChatNotFoundException
import ru.kima.intelligentchat.domain.chat.model.Chat
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

    override fun subscribeToCardChat(cardId: Long): Flow<FullChat> {
        return chatDao.subscribeToCardChat(cardId).map {
            if (it == null) {
                throw (ChatNotFoundException())
            }
            it.toFullChat()
        }
    }

    override suspend fun deleteChat(chatId: Long): Boolean {
        return chatDao.deleteChat(chatId) > 0
    }
}