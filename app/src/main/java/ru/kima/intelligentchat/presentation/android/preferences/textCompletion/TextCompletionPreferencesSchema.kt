package ru.kima.intelligentchat.presentation.android.preferences.textCompletion

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.domain.preferences.textCompletion.TextCompletionPreferences

@Serializable
data class TextCompletionPreferencesSchema(
    val baseUrl: String = "",
    val selectedPreset: Long = 1L
)

val Context.textCompletionPreferencesDataStore: DataStore<TextCompletionPreferencesSchema> by dataStore(
    fileName = "text_completion_preferences.pb",
    serializer = TextCompletionPreferencesSerializer
)

fun TextCompletionPreferencesSchema.toPreferences() = TextCompletionPreferences(
    baseUrl = baseUrl,
    selectedPreset = selectedPreset
)
