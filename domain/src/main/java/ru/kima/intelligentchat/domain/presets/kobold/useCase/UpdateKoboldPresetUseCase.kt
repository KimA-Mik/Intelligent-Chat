package ru.kima.intelligentchat.domain.presets.kobold.useCase

import ru.kima.intelligentchat.domain.presets.kobold.model.KoboldPreset
import ru.kima.intelligentchat.domain.presets.kobold.repository.KoboldPresetRepository

class UpdateKoboldPresetUseCase(private val repository: KoboldPresetRepository) {
    suspend operator fun invoke(preset: KoboldPreset) = repository.updatePreset(preset)
}