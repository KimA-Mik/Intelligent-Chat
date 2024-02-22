package ru.kima.intelligentchat.domain.horde.useCase

import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.horde.model.ActiveModel
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository

class GetActiveModelsUseCase(private val repository: HordeRepository) {
    suspend operator fun invoke(): Resource<List<ActiveModel>> {
        return repository.activeModels()
    }
}