package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordePreferences.HordePreferencesHandler

class SelectHordeModelsUseCase(private val preferences: HordePreferencesHandler) {
    suspend operator fun invoke(models: List<String>) {
        preferences.selectModels(models)
    }
}