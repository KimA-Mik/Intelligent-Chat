package ru.kima.intelligentchat.domain.horde.model

object HordeReturnCodes {
    //Something went wrong when transferring kudos. This is a base rc, so you should never typically see it.
    const val KUDOS_VALIDATION_ERROR = "KudosValidationError"

    //Source image validation failed unexpectedly
    const val IMAGE_VALIDATION_FAILED = "ImageValidationFailed"

    //Invalid AI Horde API key provided
    const val INVALID_APIKEY = "InvalidAPIKey"

    //User has requested too many generations concurrently
    const val TOO_MANY_PROMPTS = "TooManyPrompts"

    //Request aborted because horde is in maintenance mode
    const val MAINTENANCE_MODE = "MaintenanceMode"

    //Request not found. This probably means it was deleted for inactivity
    const val REQUEST_NOT_FOUND = "RequestNotFound"
}