package ru.kima.intelligentchat.domain.common.errors

sealed interface HordeError : Error {
    data object NoConnection : HordeError
    data class ValidationError(val message: String, val additionalPrompts: List<String>) :
        HordeError

    data object InvalidApiKey : HordeError
    data object TooManyPrompts : HordeError
    data object MaintenanceMode : HordeError
    data class UnknownError(val message: String? = null) : HordeError
}