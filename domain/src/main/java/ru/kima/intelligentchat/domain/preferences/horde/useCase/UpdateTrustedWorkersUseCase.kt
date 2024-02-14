package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordePreferences.HordePreferencesHandler

class UpdateTrustedWorkersUseCase(private val handler: HordePreferencesHandler) {
    suspend operator fun invoke(trustedWorkers: Boolean) {
        handler.updateTrustedWorkers(trustedWorkers)
    }
}