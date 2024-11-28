package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository

class UpdateTrustedWorkersUseCase(
    private val repository: HordeStateRepository

) {
    suspend operator fun invoke(trustedWorkers: Boolean) {
        repository.updateTrustedWorkers(trustedWorkers)
    }
}