package ru.kima.intelligentchat.data.card.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.TAGS_TABLE_NAME

@Entity(tableName = TAGS_TABLE_NAME)
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val value: String
)