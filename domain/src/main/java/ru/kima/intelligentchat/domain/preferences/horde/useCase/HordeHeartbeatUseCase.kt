package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository

//TODO: Figure out the best way ko keep connection state
class HordeHeartbeatUseCase(private val repository: HordeRepository) {
    suspend operator fun invoke(): HeartbeatResult {
        return when (val response = repository.heartbeat()) {
            is Resource.Success -> HeartbeatResult.Success
            else -> when (response.message) {
                "0" -> HeartbeatResult.NoInternet
                else -> HeartbeatResult.Error(
                    response.message ?: "Very unknown horde heartbeat message"
                )
            }
        }
    }

    sealed interface HeartbeatResult {
        data object Success : HeartbeatResult
        data class Error(val message: String) : HeartbeatResult
        data object NoInternet : HeartbeatResult
    }
}