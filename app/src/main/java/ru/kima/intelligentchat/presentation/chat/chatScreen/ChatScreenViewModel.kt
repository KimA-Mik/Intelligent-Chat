package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent

class ChatScreenViewModel(
    val savedStateHandle: SavedStateHandle,
    val appPreferences: GetPreferencesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ChatScreenState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            appPreferences().collect {
                _state.value = ChatScreenState(it.selectedPersonaId)
            }
        }
    }

    fun onEvent(event: UserEvent) {

    }
}