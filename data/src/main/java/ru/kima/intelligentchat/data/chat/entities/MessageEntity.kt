package ru.kima.intelligentchat.data.chat.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.kima.intelligentchat.data.MESSAGES_TABLE_NAME
import ru.kima.intelligentchat.data.chat.types.sender.SenderType
import ru.kima.intelligentchat.data.chat.types.sender.SenderTypeConverter

@Entity(tableName = MESSAGES_TABLE_NAME)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val messageId: Long,
    @TypeConverters(SenderTypeConverter::class)
    val sender: SenderType,
    val senderId: Long,
    val index: Int,
    val selectedSwipeIndex: Int
)
