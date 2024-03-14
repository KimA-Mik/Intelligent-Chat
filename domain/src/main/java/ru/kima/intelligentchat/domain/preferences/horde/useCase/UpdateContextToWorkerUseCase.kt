package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler

class UpdateContextToWorkerUseCase(private val handler: HordeStateHandler) {
    suspend operator fun invoke(contextToWorker: Boolean) {
        handler.updateContextToWorker(contextToWorker)
    }
}