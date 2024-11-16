package ru.kima.intelligentchat.data.chat.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.SWIPE_TABLE_NAME

@Entity(tableName = SWIPE_TABLE_NAME)
data class SwipeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "swipe_id")
    val swipeId: Long,
    @ColumnInfo(name = "message_id")
    val messageId: Long,
    val text: String,
    @ColumnInfo(defaultValue = "0")
    val deleted: Boolean,
)