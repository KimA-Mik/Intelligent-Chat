package ru.kima.intelligentchat.presentation.android.preferences.textCompletion

import android.content.Context
import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.domain.preferences.textCompletion.TextCompletionPreferencesRepository

class TextCompletionPreferencesRepositoryImpl(
    context: Context
) : TextCompletionPreferencesRepository {
    private val store = context.textCompletionPreferencesDataStore

    override fun preferences() = store.data.map(TextCompletionPreferencesSchema::toPreferences)

    override suspend fun updateBaseUrl(baseUrl: String) {
        store.updateData { it.copy(baseUrl = baseUrl) }
    }

    override suspend fun selectPreset(presetId: Long) {
        store.updateData { it.copy(selectedPreset = presetId) }
    }
}
