package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository

class UpdateHordeUserDataUseCase(
    private val repository: HordeStateRepository
) {
    suspend operator fun invoke(
        apiKey: String = String(),
        userName: String = String(),
        userId: Int = 0,
    ) {
        repository.updateUserData(apiKey, userName, userId)
    }
}