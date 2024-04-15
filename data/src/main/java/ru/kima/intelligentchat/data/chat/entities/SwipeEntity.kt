package ru.kima.intelligentchat.data.chat.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.SWIPE_TABLE_NAME

@Entity(tableName = SWIPE_TABLE_NAME)
data class SwipeEntity(
    @PrimaryKey(autoGenerate = true)
    val messageSwipeId: Long,
    val messageId: Long,
    val text: String
)