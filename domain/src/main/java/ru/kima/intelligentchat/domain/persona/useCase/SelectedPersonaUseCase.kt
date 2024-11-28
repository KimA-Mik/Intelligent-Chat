package ru.kima.intelligentchat.domain.persona.useCase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository
import ru.kima.intelligentchat.domain.preferences.app.AppPreferencesRepository
import kotlin.coroutines.CoroutineContext

class SelectedPersonaUseCase(
    private val repository: PersonaRepository,
    private val preferencesRepository: AppPreferencesRepository,
) {
    operator fun invoke(context: CoroutineContext = Dispatchers.IO) =
        invoke(CoroutineScope(context))

    operator fun invoke(scope: CoroutineScope): Flow<Persona> = channelFlow {
        var personaJob: Job? = null
        preferencesRepository.preferences()
            .map { it.selectedPersonaId }
            .distinctUntilChanged()
            .collect { selectedPersonaId ->
                personaJob?.cancel()
                personaJob = scope.launch {
                    repository.subscribeToPersona(selectedPersonaId)
                        .filterNotNull()
                        .collect {
                            send(it)
                        }
                }
            }
    }
}