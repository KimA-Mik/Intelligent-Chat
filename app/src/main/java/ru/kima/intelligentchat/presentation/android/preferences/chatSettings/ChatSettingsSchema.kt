package ru.kima.intelligentchat.presentation.android.preferences.chatSettings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.domain.preferences.chatSettings.ChatSettings

@Serializable
data class ChatSettingsSchema(
    val showDate: Boolean = true,
    val showNumber: Boolean = true,
)

val Context.chatAppearanceDataStore: DataStore<ChatSettingsSchema> by dataStore(
    fileName = "chat_settings.pb",
    serializer = ChatSettingsSerializer
)

fun ChatSettingsSchema.toChatAppearance(): ChatSettings {
    return ChatSettings(
        showDate = showDate,
        showNumber = showNumber,
    )
}