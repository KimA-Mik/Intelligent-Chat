package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository

class UpdateContextToWorkerUseCase(
    private val repository: HordeStateRepository

) {
    suspend operator fun invoke(contextToWorker: Boolean) {
        repository.updateContextToWorker(contextToWorker)
    }
}