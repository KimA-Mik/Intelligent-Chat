package ru.kima.intelligentchat.presentation.connection.overview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent

class ConnectionOverviewViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state = MutableStateFlow(ConnectionOverviewState())
    val state = _state.asStateFlow()

    fun onEvent(event: COUserEvent) {

    }
}