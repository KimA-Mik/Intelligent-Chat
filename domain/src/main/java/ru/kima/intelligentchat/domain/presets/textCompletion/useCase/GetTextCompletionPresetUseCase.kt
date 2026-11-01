package ru.kima.intelligentchat.domain.presets.textCompletion.useCase

import ru.kima.intelligentchat.domain.presets.kobold.model.KoboldPreset
import ru.kima.intelligentchat.domain.presets.textCompletion.repository.TextCompletionPresetRepository

class GetTextCompletionPresetUseCase(
    private val repository: TextCompletionPresetRepository
) {
    suspend operator fun invoke(id: Long): KoboldPreset? = repository.getPreset(id)
}
