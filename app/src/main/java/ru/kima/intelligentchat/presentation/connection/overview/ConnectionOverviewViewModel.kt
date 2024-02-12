package ru.kima.intelligentchat.presentation.connection.overview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.core.common.API_TYPE
import ru.kima.intelligentchat.domain.preferences.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.domain.preferences.useCase.UpdateSelectedApiUseCase
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent

class ConnectionOverviewViewModel(
    private val savedStateHandle: SavedStateHandle,
    getPreferences: GetPreferencesUseCase,
    private val updateSelectedApi: UpdateSelectedApiUseCase
) : ViewModel() {
    val state = combine(getPreferences()) {
        ConnectionOverviewState(selectedApiType = it.first().selectedApiType)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ConnectionOverviewState())

    fun onEvent(event: COUserEvent) {
        when (event) {
            is COUserEvent.UpdateSelectedApi -> onUpdateSelectedApi(event.apiType)
        }
    }

    private fun onUpdateSelectedApi(apiType: API_TYPE) = viewModelScope.launch {
        updateSelectedApi(apiType)
    }
}