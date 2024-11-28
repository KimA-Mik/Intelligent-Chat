package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository

class UpdateResponseToWorkerUseCase(
    private val repository: HordeStateRepository
) {
    suspend operator fun invoke(responseToWorker: Boolean) {
        repository.updateResponseToWorker(responseToWorker)
    }
}