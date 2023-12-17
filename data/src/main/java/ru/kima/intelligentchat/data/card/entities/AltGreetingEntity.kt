package ru.kima.intelligentchat.data.card.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.ALT_GREETING_TABLE_NAME

@Entity(tableName = ALT_GREETING_TABLE_NAME)
data class AltGreetingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cardId: Long = 0,
    val body: String
)
