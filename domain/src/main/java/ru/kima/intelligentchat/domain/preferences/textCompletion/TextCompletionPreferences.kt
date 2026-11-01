package ru.kima.intelligentchat.domain.preferences.textCompletion

data class TextCompletionPreferences(
    val baseUrl: String,
    val selectedPreset: Long
)
