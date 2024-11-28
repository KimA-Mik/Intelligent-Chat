package ru.kima.intelligentchat.domain.preferences.app.useCase

import ru.kima.intelligentchat.domain.preferences.app.AppPreferencesRepository

class SetSelectedPersonaIdUseCase(
    private val repository: AppPreferencesRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.updateSelectedPersona(id)
    }
}