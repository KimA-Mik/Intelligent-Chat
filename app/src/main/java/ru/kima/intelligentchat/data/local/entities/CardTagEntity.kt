package ru.kima.intelligentchat.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.local.CARD_TAGS_TABLE_NAME

@Entity(tableName = CARD_TAGS_TABLE_NAME)
data class CardTagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val characterId: Int,
    val tagId: Int
)
