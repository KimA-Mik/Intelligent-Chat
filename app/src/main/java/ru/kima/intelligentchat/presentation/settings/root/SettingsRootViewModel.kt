package ru.kima.intelligentchat.presentation.settings.root

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.settings.root.events.SettingsRootAction
import ru.kima.intelligentchat.presentation.settings.root.events.SettingsRootUiEvent

class SettingsRootViewModel : ViewModel() {
    private val _uiEvent = MutableStateFlow<Event<SettingsRootUiEvent>>(Event(null))
    val uiEvent = _uiEvent.asStateFlow()

    fun onEvent(action: SettingsRootAction) {
        when (action) {
            SettingsRootAction.OpenAdvancedFormatting -> onOpenAdvancedFormatting()
            SettingsRootAction.OpenChatAppearance -> onOpenChatAppearance()
        }
    }

    private fun onOpenAdvancedFormatting() {
        _uiEvent.value = Event(SettingsRootUiEvent.NotImplemented)
    }

    private fun onOpenChatAppearance() {
        _uiEvent.value = Event(SettingsRootUiEvent.NavigateToChatAppearance)
    }
}