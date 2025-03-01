package ru.kima.intelligentchat.domain.horde.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.common.Resource
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository
import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository

class GetKudosUseCase(
    private val repository: HordeRepository,
    private val hordeStateRepository: HordeStateRepository,
) {
    suspend operator fun invoke(): GetKudosResult {
        val pref = hordeStateRepository.hordeState().first()
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