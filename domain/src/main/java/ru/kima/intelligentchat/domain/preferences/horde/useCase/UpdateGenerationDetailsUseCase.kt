package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordePreferences.HordePreferencesHandler

class UpdateGenerationDetailsUseCase(private val preferencesHandler: HordePreferencesHandler) {
    suspend operator fun invoke(
        contextSize: Int = -1,
        responseLength: Int = -1
    ) {
        preferencesHandler.updateGenerationDetails(contextSize, responseLength)
    }
}