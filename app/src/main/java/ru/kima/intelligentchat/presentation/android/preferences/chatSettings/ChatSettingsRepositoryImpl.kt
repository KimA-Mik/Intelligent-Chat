package ru.kima.intelligentchat.presentation.android.preferences.chatSettings

import android.content.Context
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.domain.preferences.Preference
import ru.kima.intelligentchat.domain.preferences.chatSettings.ChatSettingsRepository

class ChatSettingsRepositoryImpl(
    context: Context
) : ChatSettingsRepository {
    private val store = context.chatAppearanceDataStore

    override val showDate = object : Preference<Boolean> {
        override fun subscribe() = store.data.map { it.showDate }
        override suspend fun set(value: Boolean) {
            store.updateData {
                it.copy(
                    showDate = value
                )
            }
        }
    }

    override val showNumber = object : Preference<Boolean> {
        override fun subscribe() = store.data.map { it.showNumber }
        override suspend fun set(value: Boolean) {
            store.updateData {
                it.copy(
                    showNumber = value
                )
            }
        }
    }
}