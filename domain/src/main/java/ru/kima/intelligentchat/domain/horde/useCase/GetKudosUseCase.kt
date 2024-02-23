package ru.kima.intelligentchat.domain.horde.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.core.preferences.hordePreferences.HordePreferencesHandler
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository

class GetKudosUseCase(
    private val repository: HordeRepository,
    private val preferences: HordePreferencesHandler
) {
    suspend operator fun invoke(): GetKudosResult {
        val pref = preferences.data.first()
        if (pref.userId == 0) {
            return GetKudosResult.NoUser
        }

        val apiToken = pref.apiToken
        return when (val result = repository.findUser(apiToken)) {
            is Resource.Success -> GetKudosResult.Success(result.data!!.kudos)
            else -> when (result.message) {
                "0" -> GetKudosResult.NoInternet
                else -> GetKudosResult.UnknownError(result.message ?: "Very unknown message")
            }
        }
    }

    sealed interface GetKudosResult {
        data class Success(val kudos: Double) : GetKudosResult
        data object NoUser : GetKudosResult
        data class UnknownError(val message: String) : GetKudosResult
        data object NoInternet : GetKudosResult
    }
}