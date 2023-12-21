package ru.kima.intelligentchat.presentation.personas.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kima.intelligentchat.presentation.personas.list.events.UserEvent

class PersonasListViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(PersonasListState())
    val state = _state.asStateFlow()

    fun onEvent(event: UserEvent) {

    }
}