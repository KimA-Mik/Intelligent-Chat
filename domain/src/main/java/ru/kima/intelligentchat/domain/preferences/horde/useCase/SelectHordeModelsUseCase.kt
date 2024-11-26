package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository

class SelectHordeModelsUseCase(
    private val repository: HordeStateRepository

) {
    suspend operator fun invoke(models: List<String>) {
        repository.selectModels(models)
    }
}