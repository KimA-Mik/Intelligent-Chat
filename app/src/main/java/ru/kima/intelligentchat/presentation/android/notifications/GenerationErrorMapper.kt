package ru.kima.intelligentchat.presentation.android.notifications

import android.content.Context
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.domain.common.errors.GenerationError
import ru.kima.intelligentchat.domain.common.errors.HordeError

fun GenerationError.getDescription(context: Context): String {
    return when (this) {
        is HordeError -> hordeErrorDescription(context, this)
        GenerationError.NotImplemented -> context.getString(R.string.generation_error_description_not_implemented)
    }
}

private fun hordeErrorDescription(context: Context, error: HordeError): String {
    return when (error) {
        HordeError.InvalidApiKey -> context.getString(R.string.horde_error_description_invalid_api_key)
        HordeError.MaintenanceMode -> context.getString(R.string.horde_error_description_maintenance_mode)
        HordeError.NoConnection -> context.getString(R.string.generation_error_description_no_internet_connection)
        HordeError.RequestNotFound -> context.getString(R.string.horde_error_description_request_not_found)
        HordeError.TooManyPrompts -> context.getString(R.string.horde_error_description_too_many_prompts)
        is HordeError.UnknownError -> context.getString(
            R.string.horde_error_description_unknown_error,
            error.message
        )

        is HordeError.ValidationError -> context.getString(
            R.string.horde_error_description_validation_error,
            error.message,
            error.additionalPrompts.joinToString("\n")
        )
    }
}