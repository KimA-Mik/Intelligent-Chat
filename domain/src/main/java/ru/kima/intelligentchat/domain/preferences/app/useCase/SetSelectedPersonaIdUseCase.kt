package ru.kima.intelligentchat.domain.preferences.app.useCase

import ru.kima.intelligentchat.core.preferences.appPreferences.PreferencesHandler

class SetSelectedPersonaIdUseCase(private val preferencesHandler: PreferencesHandler) {
    suspend operator fun invoke(id: Long) {
        preferencesHandler.updateSelectedPersona(id)
    }
}