package ru.kima.intelligentchat.presentation.settings.settingsScreens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.preferences.chatSettings.ChatSettingsRepository
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ImmutableChatAppearance
import ru.kima.intelligentchat.presentation.settings.settingsScreens.chat.events.ChatSettingsAction

class ChatSettingsViewModel(
    private val chatSettingsRepository: ChatSettingsRepository
) : ViewModel() {
    val chatAppearance = combine(
        chatSettingsRepository.showDate.subscribe(),
        chatSettingsRepository.showNumber.subscribe()
    ) { showDate, showNumber ->
        ImmutableChatAppearance(
            showDate = showDate,
            showNumber = showNumber
        )
    }

    fun onEvent(action: ChatSettingsAction) {
        when (action) {
            is ChatSettingsAction.SetShowDate -> onSetShowDate(action.value)
            is ChatSettingsAction.SetShowNumber -> onSetShowNumber(action.value)
        }
    }

    private fun onSetShowDate(value: Boolean) = viewModelScope.launch {
        chatSettingsRepository.showDate.set(value)
    }

    private fun onSetShowNumber(value: Boolean) = viewModelScope.launch {
        chatSettingsRepository.showNumber.set(value)
    }
}