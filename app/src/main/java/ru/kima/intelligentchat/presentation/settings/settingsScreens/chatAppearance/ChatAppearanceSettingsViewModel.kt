package ru.kima.intelligentchat.presentation.settings.settingsScreens.chatAppearance

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ChatAppearance
import ru.kima.intelligentchat.presentation.settings.settingsScreens.chatAppearance.events.ChatAppearanceSettingsAction

class ChatAppearanceSettingsViewModel : ViewModel() {
    private val _chatAppearance = MutableStateFlow(ChatAppearance())
    val chatAppearance = _chatAppearance.asStateFlow()

    fun onEvent(action: ChatAppearanceSettingsAction) {
        when (action) {
            is ChatAppearanceSettingsAction.SetShowDate -> onSetShowDate(action.value)
            is ChatAppearanceSettingsAction.SetShowNumber -> onSetShowNumber(action.value)
        }
    }

    private fun onSetShowDate(value: Boolean) {
        _chatAppearance.update {
            it.copy(showDate = value)
        }
    }

    private fun onSetShowNumber(value: Boolean) {
        _chatAppearance.update {
            it.copy(showNumber = value)
        }
    }
}