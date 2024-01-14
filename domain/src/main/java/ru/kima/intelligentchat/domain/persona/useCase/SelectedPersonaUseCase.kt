package ru.kima.intelligentchat.domain.persona.useCase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.kima.intelligentchat.core.preferences.AppPreferences
import ru.kima.intelligentchat.core.preferences.PreferencesHandler
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository

class SelectedPersonaUseCase(
    private val repository: PersonaRepository,
    private val preferences: PreferencesHandler
) {
    private var selectedPersona = 0L
    private var job: Job? = null
    private val state = MutableStateFlow(Persona())
    operator fun invoke(scope: CoroutineScope): Flow<Persona> {
        preferences.data
            .onEach { collectPreferences(it, scope) }
            .launchIn(scope)

        return state
    }

    private fun collectPreferences(preferences: AppPreferences, scope: CoroutineScope) {
        val id = preferences.selectedPersonaId
        if (selectedPersona != id) {
            job?.cancel()
            selectedPersona = id
            job = repository
                .subscribeToPersona(selectedPersona)
                .filterNotNull()
                .onEach { state.value = it }
                .launchIn(scope)
        }
    }
}