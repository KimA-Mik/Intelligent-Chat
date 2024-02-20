package ru.kima.intelligentchat.domain.horde.useCase

import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.horde.model.UserInfo
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateHordeUserDataUseCase

class SaveApiKeyUseCase(
    private val repository: HordeRepository,
    private val updateHordeUserDetails: UpdateHordeUserDataUseCase
) {
    suspend operator fun invoke(apiKey: String): Resource<UserInfo> {
        val result = repository.findUser(apiKey)

        when (result) {
            is Resource.Success -> {
                updateHordeUserDetails(
                    apiKey = apiKey,
                    userName = result.data!!.userName,
                    userId = result.data!!.id
                )
            }

            is Resource.Error -> {
                updateHordeUserDetails()
            }

            else -> {}
        }

        return result
    }
}