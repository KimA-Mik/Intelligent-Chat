package ru.kima.intelligentchat.data.chat.advancedFormatting.instructMode

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kima.intelligentchat.data.INSTRUCT_MODE_TEMPLATES_TABLE_NAME

@Entity(tableName = INSTRUCT_MODE_TEMPLATES_TABLE_NAME)
data class InstructModeTemplateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(defaultValue = "Default")
    val name: String,
    @ColumnInfo(name = "include_name_policy", defaultValue = "ALWAYS")
    val includeNamePolicy: IncludeNamePolicyDto,
    @ColumnInfo(name = "wrap_sequences_with_new_line", defaultValue = "1")
    val wrapSequencesWithNewLine: Boolean,
    @ColumnInfo(name = "user_message_prefix", defaultValue = "")
    val userMessagePrefix: String,
    @ColumnInfo(name = "user_message_postfix", defaultValue = "")
    val userMessagePostfix: String,
    @ColumnInfo(name = "assistant_message_prefix", defaultValue = "")
    val assistantMessagePrefix: String,
    @ColumnInfo(name = "assistant_message_postfix", defaultValue = "")
    val assistantMessagePostfix: String,
    @ColumnInfo(name = "system_same_as_user", defaultValue = "0")
    val systemSameAsUser: Boolean,
    @ColumnInfo(name = "first_assistant_prefix", defaultValue = "")
    val firstAssistantPrefix: String,
    @ColumnInfo(name = "last_assistant_prefix", defaultValue = "")
    val lastAssistantPrefix: String,
    @ColumnInfo(name = "first_user_prefix", defaultValue = "")
    val firstUserPrefix: String,
    @ColumnInfo(name = "last_user_prefix", defaultValue = "")
    val lastUserPrefix: String,
)