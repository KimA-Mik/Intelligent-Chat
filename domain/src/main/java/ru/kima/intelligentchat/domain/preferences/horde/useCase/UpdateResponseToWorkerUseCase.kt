package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler

class UpdateResponseToWorkerUseCase(private val handler: HordeStateHandler) {
    suspend operator fun invoke(responseToWorker: Boolean) {
        handler.updateResponseToWorker(responseToWorker)
    }
}