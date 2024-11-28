package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository

class UpdateUserGenerationDetailsUseCase(
    private val repository: HordeStateRepository
) {
    suspend operator fun invoke(
        contextSize: Int = -1,
        responseLength: Int = -1
    ) {
        repository.updateUserGenerationDetails(contextSize, responseLength)
    }
}