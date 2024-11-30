package ru.kima.intelligentchat.presentation.android.preferences.chatSettings

import android.content.Context
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.domain.preferences.chatSettings.ChatSettingsRepository

class ChatSettingsRepositoryImpl(
    context: Context
) : ChatSettingsRepository {
    private val store = context.chatAppearanceDataStore
    override fun chatSettings() = store.data.map {
        it.toChatAppearance()
    }

    override suspend fun updateShowDate(value: Boolean) {
        updateData {
            it.copy(
                showDate = value
            )
        }
    }

    override suspend fun updateShowNumber(value: Boolean) {
        updateData {
            it.copy(
                showNumber = value
            )
        }
    }

    private suspend fun updateData(
        transform: suspend (ChatSettingsSchema) -> ChatSettingsSchema
    ): ChatSettingsSchema {
        return store.updateData(transform)
    }
}