package ru.kima.intelligentchat.data.chat.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.MESSAGES_TABLE_NAME

@Entity(tableName = MESSAGES_TABLE_NAME)
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val chatMessageId: Long,
    val sender: Int,
    val senderId: Long,
    val index: Int,
    val selectedSwipeIndex: Int
)
