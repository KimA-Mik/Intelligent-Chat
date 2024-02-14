package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordePreferences.HordePreferencesHandler

class UpdateContextToWorkerUseCase(private val handler: HordePreferencesHandler) {
    suspend operator fun invoke(contextToWorker: Boolean) {
        handler.updateContextToWorker(contextToWorker)
    }
}