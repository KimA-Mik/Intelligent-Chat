package ru.kima.intelligentchat.domain.presets.textCompletion.useCase

import ru.kima.intelligentchat.domain.presets.textCompletion.repository.TextCompletionPresetRepository

class SubscribeToTextCompletionPresetsUseCase(
    private val repository: TextCompletionPresetRepository
) {
    operator fun invoke() = repository.subscribeToPresets()
}
