package ru.kima.intelligentchat.domain.horde.useCase

import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.horde.model.UserInfo
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository

class SaveApiKeyUseCase(private val repository: HordeRepository) {
    suspend operator fun invoke(apiKey: String): Resource<UserInfo> {
        return repository.findUser(apiKey)
    }
}