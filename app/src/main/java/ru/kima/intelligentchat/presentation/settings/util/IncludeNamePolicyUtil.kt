package ru.kima.intelligentchat.presentation.settings.util

import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.ComposeString
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.IncludeNamePolicy

fun IncludeNamePolicy.composeSting(): ComposeString {
    return when (this) {
        IncludeNamePolicy.NEVER -> ComposeString.Resource(R.string.include_string_policy_never)
        IncludeNamePolicy.ALWAYS -> ComposeString.Resource(R.string.include_string_policy_always)
    }
}