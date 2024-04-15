package ru.kima.intelligentchat.data.chat.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.CHATS_TABLE_NAME

@Entity(tableName = CHATS_TABLE_NAME)
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val cardId: Long
)