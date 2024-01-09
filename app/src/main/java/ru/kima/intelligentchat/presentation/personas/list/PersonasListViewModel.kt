package ru.kima.intelligentchat.presentation.personas.list

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.core.preferences.PreferencesHandler
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.useCase.CreatePersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.GetPersonasUseCase
import ru.kima.intelligentchat.domain.persona.useCase.LoadPersonaImageUseCase
import ru.kima.intelligentchat.presentation.personas.list.events.UiEvent
import ru.kima.intelligentchat.presentation.personas.list.events.UserEvent
import ru.kima.intelligentchat.presentation.personas.list.model.toListItem

class PersonasListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getPersonas: GetPersonasUseCase,
    private val createPersona: CreatePersonaUseCase,
    private val getPersonaImage: LoadPersonaImageUseCase,
    private val preferencesHandler: PreferencesHandler
) : ViewModel() {
    private val personas = MutableStateFlow(emptyList<Persona>())
    private val selectedPersona = MutableStateFlow(0L)
    private val personaItems = combine(
        personas,
        selectedPersona
    ) { personas, selectedPersona ->
        Log.d("PersonasListViewModel", "personaItems = combine")
        personas.map { it.toListItem(selected = selectedPersona == it.id) }.toImmutableList()
    }

    private val thumbnails = MutableStateFlow<ImmutableList<Bitmap?>>(persistentListOf())

    //    private val personaItems = MutableStateFlow(emptyList<PersonaItem>())
    private val query = savedStateHandle.getStateFlow("query", String())
    val state = combine(
        personaItems,
        thumbnails,
        query,
    ) { personaListItems, thumbnails, query ->
        PersonasListState(
            personas = personaListItems,
            thumbnails = thumbnails,
            query = query,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PersonasListState())


    private val _events = MutableStateFlow<Event<UiEvent>>(Event(null))
    val events = _events.asStateFlow()

    init {
        getPersonas(query.value).onEach {
            personas.value = it

            thumbnails.value = it.map { persona ->
                getPersonaImage(persona.id).bitmap
            }.toImmutableList()
        }.launchIn(viewModelScope)

        preferencesHandler.data.onEach {
            selectedPersona.value = it.selectedPersonaId
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.NavigateToPersona -> onNavigateToPersona(event.id)
            is UserEvent.QueryChanged -> onQueryChanged(event.query)
            UserEvent.CreatePersona -> onCreatePersona()
            is UserEvent.SelectPersona -> onSelectPersona(event.id)
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

    private fun onSelectPersona(id: Long) = viewModelScope.launch {
        if (selectedPersona.value != id) {
            preferencesHandler.updateSelectedPersona(id)
        }
    }
}