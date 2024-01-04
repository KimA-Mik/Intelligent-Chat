package ru.kima.intelligentchat.presentation.personas.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.useCase.CreatePersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.GetPersonasUseCase
import ru.kima.intelligentchat.presentation.personas.list.events.UiEvent
import ru.kima.intelligentchat.presentation.personas.list.events.UserEvent

class PersonasListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getPersonas: GetPersonasUseCase,
    private val createPersona: CreatePersonaUseCase
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


    private val _events = MutableStateFlow<Event<UiEvent>>(Event(null))
    val events = _events.asStateFlow()

    init {
        getPersonas(query.value).onEach {
            personas.value = it
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.NavigateToPersona -> onNavigateToPersona(event.id)
            is UserEvent.QueryChanged -> onQueryChanged(event.query)
            UserEvent.CreatePersona -> onCreatePersona()
        }
    }

    private fun onNavigateToPersona(id: Long) = viewModelScope.launch {
        _events.emit(Event(UiEvent.NavigateToPersona(id)))
    }

    private fun onQueryChanged(query: String) {
        savedStateHandle["query"] = query
    }

    private fun onCreatePersona() = viewModelScope.launch {
        val id = createPersona(Persona())
        _events.emit(Event(UiEvent.NavigateToPersona(id)))
    }
}