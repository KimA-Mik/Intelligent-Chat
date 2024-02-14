package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordePreferences.HordePreferencesHandler

class UpdateHordeApiTokenUseCase(private val handler: HordePreferencesHandler) {
    suspend operator fun invoke(apiKey: String) {
        handler.updateApiKey(apiKey)
    }
}