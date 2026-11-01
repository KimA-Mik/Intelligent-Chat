package ru.kima.intelligentchat.domain.presets.textCompletion.useCase

import ru.kima.intelligentchat.domain.presets.kobold.model.KoboldPreset
import ru.kima.intelligentchat.domain.presets.textCompletion.repository.TextCompletionPresetRepository

class UpdateTextCompletionPresetUseCase(
    private val repository: TextCompletionPresetRepository
) {
    suspend operator fun invoke(preset: KoboldPreset) = repository.updatePreset(preset)
}
