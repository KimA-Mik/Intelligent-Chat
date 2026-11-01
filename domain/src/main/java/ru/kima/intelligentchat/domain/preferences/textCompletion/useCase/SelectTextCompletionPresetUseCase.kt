package ru.kima.intelligentchat.domain.preferences.textCompletion.useCase

import ru.kima.intelligentchat.domain.preferences.textCompletion.TextCompletionPreferencesRepository

class SelectTextCompletionPresetUseCase(
    private val repository: TextCompletionPreferencesRepository
) {
    suspend operator fun invoke(presetId: Long) = repository.selectPreset(presetId)
}
