package ru.kima.intelligentchat.presentation.android.preferences.chatAppearance

import android.content.Context
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.domain.preferences.chatAppearance.ChatAppearanceRepository

class ChatAppearanceRepositoryImpl(
    context: Context
) : ChatAppearanceRepository {
    private val store = context.chatAppearanceDataStore
    override fun chatAppearance() = store.data.map {
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
        transform: suspend (ChatAppearanceSchema) -> ChatAppearanceSchema
    ): ChatAppearanceSchema {
        return store.updateData(transform)
    }
}