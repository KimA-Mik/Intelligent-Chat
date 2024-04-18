package ru.kima.intelligentchat.data.chat.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.kima.intelligentchat.data.MESSAGES_TABLE_NAME
import ru.kima.intelligentchat.data.chat.types.sender.SenderTypeConverter
import ru.kima.intelligentchat.data.chat.types.sender.SenderTypeDto

@Entity(tableName = MESSAGES_TABLE_NAME)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "message_id")
    val messageId: Long,
    @ColumnInfo(name = "chat_id")
    val chatId: Long,
    @TypeConverters(SenderTypeConverter::class)
    val sender: SenderTypeDto,
    @ColumnInfo(name = "sender_id")
    val senderId: Long,
    val index: Int,
    @ColumnInfo(name = "selected_swipe_index")
    val selectedSwipeIndex: Int
)
