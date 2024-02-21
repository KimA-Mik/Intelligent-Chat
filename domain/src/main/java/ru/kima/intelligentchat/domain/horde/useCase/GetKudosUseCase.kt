package ru.kima.intelligentchat.domain.horde.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.core.preferences.hordePreferences.HordePreferencesHandler
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository

class GetKudosUseCase(
    private val repository: HordeRepository,
    private val preferences: HordePreferencesHandler
) {
    suspend operator fun invoke(): KudosResult {
        val pref = preferences.data.first()
        if (pref.apiToken.isBlank()) {
            return KudosResult.NoUser
        }

        val result = repository.findUser(pref.apiToken)
        return when (result) {
            is Resource.Success -> KudosResult.Success(result.data!!.kudos)
            else -> KudosResult.Error
        }
    }

    sealed interface KudosResult {
        data class Success(val kudos: Float) : KudosResult
        data object NoUser : KudosResult
        data object Error : KudosResult
    }
}