package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler

class UpdateGenerationDetailsUseCase(private val preferencesHandler: HordeStateHandler) {
    suspend operator fun invoke(
        contextSize: Int = -1,
        responseLength: Int = -1
    ) {
        preferencesHandler.updateGenerationDetails(contextSize, responseLength)
    }
}