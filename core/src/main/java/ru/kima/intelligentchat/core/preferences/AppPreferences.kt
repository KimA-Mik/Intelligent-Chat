package ru.kima.intelligentchat.core.preferences

import kotlinx.serialization.Serializable

@Serializable
data class AppPreferences(
    val selectedPersonaId: Long = 0
)
