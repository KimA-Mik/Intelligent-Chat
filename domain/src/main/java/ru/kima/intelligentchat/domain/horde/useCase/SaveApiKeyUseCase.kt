package ru.kima.intelligentchat.domain.horde.useCase

import ru.kima.intelligentchat.domain.common.Resource
import ru.kima.intelligentchat.domain.horde.model.UserInfo
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateHordeUserDataUseCase

class SaveApiKeyUseCase(
    private val repository: HordeRepository,
    private val updateHordeUserDetails: UpdateHordeUserDataUseCase
) {
    suspend operator fun invoke(apiKey: String): SaveApiKeyResult {
        if (apiKey.isBlank()) {
            return SaveApiKeyResult.EmtpyKey
        }

        return when (val result = repository.findUser(apiKey)) {
            is Resource.Success -> {
                val userInfo = result.data!!
                updateHordeUserDetails(
                    apiKey = apiKey,
                    userName = userInfo.userName,
                    userId = userInfo.id
                )
                SaveApiKeyResult.Success(userInfo)
            }

            else -> {
                updateHordeUserDetails(apiKey = apiKey, userName = String(), userId = 0)
                when (result.message) {
                    "0" -> SaveApiKeyResult.NoInternet
                    "404" -> SaveApiKeyResult.UserNotFound
                    "400" -> SaveApiKeyResult.ValidationError
                    else -> SaveApiKeyResult.UnknownError(result.message ?: "Very unknown error")
                }
            }
        }
    }

//There are some ideas
//    sealed class SaveApiKeyException(override val cause: Throwable?) : Throwable() {
//        class UserNotFoundException(cause: Throwable) : SaveApiKeyException(cause)
//        class ValidationException(cause: Throwable) : SaveApiKeyException(cause)
//        class UnknownException(cause: Throwable) : SaveApiKeyException(cause)
//
//        companion object {
//            fun extractException(throwable: Throwable): SaveApiKeyException {
//                return if (throwable is SaveApiKeyException)
//                    throwable else UnknownException(throwable)
//            }
//        }
//    }

    sealed interface SaveApiKeyResult {
        data class Success(val userInfo: UserInfo) : SaveApiKeyResult
        data class UnknownError(val message: String) : SaveApiKeyResult
        data object UserNotFound : SaveApiKeyResult
        data object ValidationError : SaveApiKeyResult
        data object NoInternet : SaveApiKeyResult
        data object EmtpyKey : SaveApiKeyResult
    }
}