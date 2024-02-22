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

    //TODO: Improve return error handling
    //There are some ideas
    sealed class SaveApiKeyException(override val cause: Throwable?) : Throwable() {
        class UserNotFoundException(cause: Throwable) : SaveApiKeyException(cause)
        class ValidationException(cause: Throwable) : SaveApiKeyException(cause)
        class UnknownException(cause: Throwable) : SaveApiKeyException(cause)

        companion object {
            fun extractException(throwable: Throwable): SaveApiKeyException {
                return if (throwable is SaveApiKeyException)
                    throwable else UnknownException(throwable)
            }
        }
    }

    sealed interface SaveKeyResult {
        data class Success(val userInfo: UserInfo) : SaveKeyResult
        data class UnknownError(val message: String) : SaveKeyResult
        data object UserNotFound : SaveKeyResult
        data object ValidationError : SaveKeyResult
    }
}