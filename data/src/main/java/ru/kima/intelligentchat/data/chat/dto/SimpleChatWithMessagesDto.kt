package ru.kima.intelligentchat.data.chat.dto

import androidx.room.Embedded
import androidx.room.Relation
import ru.kima.intelligentchat.data.chat.entities.ChatEntity
import ru.kima.intelligentchat.data.chat.entities.MessageEntity

class SimpleChatWithMessagesDto(
    @Embedded
    val chat: ChatEntity,
    @Relation(
        parentColumn = "chat_id",
        entityColumn = "chat_id"
    )
    val messages: List<MessageEntity>
)