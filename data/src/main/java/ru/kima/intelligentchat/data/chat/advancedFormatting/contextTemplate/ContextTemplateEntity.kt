package ru.kima.intelligentchat.data.chat.advancedFormatting.contextTemplate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.CONTEXT_TEMPLATES_TABLE_NAME

@Entity(tableName = CONTEXT_TEMPLATES_TABLE_NAME)
data class ContextTemplateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    @ColumnInfo(name = "story_string")
    val storyString: String,
    @ColumnInfo(name = "example_separator")
    val exampleSeparator: String,
    @ColumnInfo(name = "chat_start")
    val chatStart: String
)
