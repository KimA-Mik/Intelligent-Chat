package ru.kima.intelligentchat.data.chat.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val chatMessageId: Long,
    val sender: Int,
    val senderId: Long,
    val index: Int,
    val selectedSwipeIndex: Int
)
