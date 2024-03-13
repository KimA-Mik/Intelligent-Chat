package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler

class SelectHordeModelsUseCase(private val preferences: HordeStateHandler) {
    suspend operator fun invoke(models: List<String>) {
        preferences.selectModels(models)
    }
}