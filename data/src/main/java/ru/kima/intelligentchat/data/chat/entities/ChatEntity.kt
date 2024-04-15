package ru.kima.intelligentchat.data.chat.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val cardId: Long
)