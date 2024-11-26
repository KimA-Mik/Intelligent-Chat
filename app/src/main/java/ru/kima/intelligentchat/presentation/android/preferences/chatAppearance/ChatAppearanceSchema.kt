package ru.kima.intelligentchat.presentation.android.preferences.chatAppearance

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.domain.preferences.chatAppearance.ChatAppearance

@Serializable
data class ChatAppearanceSchema(
    val showDate: Boolean = true,
    val showNumber: Boolean = true,
)

val Context.chatAppearanceDataStore: DataStore<ChatAppearanceSchema> by dataStore(
    fileName = "chatAppearance.pb",
    serializer = ChatAppearanceSerializer
)

fun ChatAppearanceSchema.toChatAppearance(): ChatAppearance {
    return ChatAppearance(
        showDate = showDate,
        showNumber = showNumber,
    )
}