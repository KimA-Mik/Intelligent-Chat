package ru.kima.intelligentchat.presentation.android.preferences.chatSettings

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.kima.intelligentchat.domain.preferences.Preference
import ru.kima.intelligentchat.domain.preferences.chatSettings.ChatSettingsRepository
import ru.kima.intelligentchat.presentation.android.preferences.ICPreference

class ChatSettingsRepositoryImpl(
    context: Context
) : ChatSettingsRepository {
    private val store = context.chatAppearanceDataStore
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var _current: ChatSettingsSchema = ChatSettingsSchema()

    init {
        println("ChatSettingsRepository created")
        store.data.onEach {
            _current = it
        }.launchIn(coroutineScope)
    }

    override fun showDate(): Preference<Boolean> {
        return ICPreference(
            initialValue = _current.showDate,
            flow = store.data.map { it.showDate },
            setter = { value ->
                store.updateData { it.copy(showDate = value) }
            }
        )
    }

    override fun showNumber(): Preference<Boolean> {
        return ICPreference(
            initialValue = _current.showNumber,
            flow = store.data.map { it.showNumber },
            setter = { value ->
                store.updateData { it.copy(showNumber = value) }
            }
        )
    }
}