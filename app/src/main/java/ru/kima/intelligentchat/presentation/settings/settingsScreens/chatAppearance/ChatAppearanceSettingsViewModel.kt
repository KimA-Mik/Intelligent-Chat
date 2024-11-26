package ru.kima.intelligentchat.presentation.settings.settingsScreens.chatAppearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.preferences.chatAppearance.ChatAppearanceRepository
import ru.kima.intelligentchat.presentation.chat.chatScreen.mappers.toImmutable
import ru.kima.intelligentchat.presentation.settings.settingsScreens.chatAppearance.events.ChatAppearanceSettingsAction

class ChatAppearanceSettingsViewModel(
    private val chatAppearanceRepository: ChatAppearanceRepository
) : ViewModel() {
    val chatAppearance = chatAppearanceRepository.chatAppearance().map {
        it.toImmutable()
    }

    fun onEvent(action: ChatAppearanceSettingsAction) {
        when (action) {
            is ChatAppearanceSettingsAction.SetShowDate -> onSetShowDate(action.value)
            is ChatAppearanceSettingsAction.SetShowNumber -> onSetShowNumber(action.value)
        }
    }

    private fun onSetShowDate(value: Boolean) = viewModelScope.launch {
        chatAppearanceRepository.updateShowDate(value)
    }

    private fun onSetShowNumber(value: Boolean) = viewModelScope.launch {
        chatAppearanceRepository.updateShowNumber(value)
    }
}