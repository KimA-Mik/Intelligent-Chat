package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordePreferences.HordePreferencesHandler

class UpdateResponseToWorkerUseCase(private val handler: HordePreferencesHandler) {
    suspend operator fun invoke(responseToWorker: Boolean) {
        handler.updateResponseToWorker(responseToWorker)
    }
}