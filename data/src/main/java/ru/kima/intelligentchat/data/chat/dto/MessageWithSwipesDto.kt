package ru.kima.intelligentchat.data.chat.dto

import androidx.room.Embedded
import androidx.room.Relation
import ru.kima.intelligentchat.data.chat.entities.MessageEntity
import ru.kima.intelligentchat.data.chat.entities.SwipeEntity

data class MessageWithSwipesDto(
    @Embedded val message: MessageEntity,
    @Relation(
        parentColumn = "message_id",
        entityColumn = "message_id"
    )
    val swipes: List<SwipeEntity>
)