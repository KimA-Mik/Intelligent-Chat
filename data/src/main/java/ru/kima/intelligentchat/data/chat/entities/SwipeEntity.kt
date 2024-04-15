package ru.kima.intelligentchat.data.chat.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SwipeEntity(
    @PrimaryKey(autoGenerate = true)
    val messageSwipeId: Long,
    val messageId: Long,
    val text: String
)