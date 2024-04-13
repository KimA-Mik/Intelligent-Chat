package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler

class UpdateUserGenerationDetailsUseCase(private val preferencesHandler: HordeStateHandler) {
    suspend operator fun invoke(
        contextSize: Int = -1,
        responseLength: Int = -1
    ) {
        preferencesHandler.updateUserGenerationDetails(contextSize, responseLength)
    }
}