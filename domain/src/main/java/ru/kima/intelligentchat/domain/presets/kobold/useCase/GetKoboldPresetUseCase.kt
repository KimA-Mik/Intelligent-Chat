package ru.kima.intelligentchat.domain.presets.kobold.useCase

import ru.kima.intelligentchat.domain.presets.kobold.repository.KoboldPresetRepository

class GetKoboldPresetUseCase(private val repository: KoboldPresetRepository) {
    suspend operator fun invoke(id: Long) = repository.getPreset(id)
}