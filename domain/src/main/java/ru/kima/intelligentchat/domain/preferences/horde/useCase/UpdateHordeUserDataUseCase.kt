package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordePreferences.HordePreferencesHandler

class UpdateHordeUserDataUseCase(private val handler: HordePreferencesHandler) {
    suspend operator fun invoke(
        apiKey: String = String(),
        userName: String = String(),
        userId: Int = 0,
    ) {
        handler.updateUserData(apiKey, userName, userId)
    }
}