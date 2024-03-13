package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler

class UpdateTrustedWorkersUseCase(private val handler: HordeStateHandler) {
    suspend operator fun invoke(trustedWorkers: Boolean) {
        handler.updateTrustedWorkers(trustedWorkers)
    }
}