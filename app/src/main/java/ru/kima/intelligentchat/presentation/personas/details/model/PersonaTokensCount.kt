package ru.kima.intelligentchat.presentation.personas.details.model

import androidx.compose.runtime.Immutable

@Immutable
data class PersonaTokensCount(
    val nameTokens: Int = 0,
    val descriptionTokens: Int = 0
)
