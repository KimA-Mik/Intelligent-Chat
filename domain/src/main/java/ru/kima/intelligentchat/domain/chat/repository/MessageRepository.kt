package ru.kima.intelligentchat.domain.chat.repository

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.Message

interface MessageRepository {
    fun subscribeToChatMessages(chatId: Long): Flow<List<Message>>

}