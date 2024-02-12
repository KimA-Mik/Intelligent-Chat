package ru.kima.intelligentchat.domain.preferences.useCase

import ru.kima.intelligentchat.core.preferences.PreferencesHandler

class SetSelectedPersonaIdUseCase(private val preferencesHandler: PreferencesHandler) {
    suspend operator fun invoke(id: Long) {
        preferencesHandler.updateSelectedPersona(id)
    }
}