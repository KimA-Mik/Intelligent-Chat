package ru.kima.intelligentchat.data.chat.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.CHATS_TABLE_NAME

@Entity(tableName = CHATS_TABLE_NAME)
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "chat_id")
    val chatId: Long,
    val title: String,
    @ColumnInfo(name = "card_id")
    val cardId: Long,
    @ColumnInfo(name = "selected_greeting")
    val selectedGreeting: Int
)