package ru.kima.intelligentchat.data.chat.dto

import androidx.room.Embedded
import androidx.room.Relation
import ru.kima.intelligentchat.data.chat.entities.MessageEntity
import ru.kima.intelligentchat.data.chat.entities.SwipeEntity

data class MessageWithSwipesDto(
    @Embedded val message: MessageEntity,
    @Relation(
        parentColumn = "messageId",
        entityColumn = "messageId"
    )
    val swipes: List<SwipeEntity>
)