package ru.kima.intelligentchat.presentation.personas.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.useCase.GetPersonasUseCase
import ru.kima.intelligentchat.presentation.personas.list.events.UiEvent
import ru.kima.intelligentchat.presentation.personas.list.events.UserEvent

class PersonasListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getPersonas: GetPersonasUseCase
) : ViewModel() {
    private val personas = MutableStateFlow(emptyList<Persona>())
    private val query = savedStateHandle.getStateFlow("query", String())
    val state = combine(
        personas,
        query
    ) { personas, query ->
        PersonasListState(
            personas = personas,
            query = query
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PersonasListState())

    private val _events = MutableSharedFlow<UiEvent>()
    val events = _events.asSharedFlow()

    init {
        getPersonas(query.value).onEach {
            personas.value = it
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.NavigateToPersona -> onNavigateToPersona(event.id)
            is UserEvent.QueryChanged -> onQueryChanged(event.query)
        }
    }

    private fun onNavigateToPersona(id: Long) = viewModelScope.launch {
        _events.emit(UiEvent.NavigateToPersona(id))
    }


    private fun onQueryChanged(query: String) {
        savedStateHandle["query"] = query
    }
}