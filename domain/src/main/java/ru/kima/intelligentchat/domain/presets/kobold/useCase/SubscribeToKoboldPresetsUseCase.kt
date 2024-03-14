package ru.kima.intelligentchat.domain.presets.kobold.useCase

import ru.kima.intelligentchat.domain.presets.kobold.repository.KoboldPresetRepository

class SubscribeToKoboldPresetsUseCase(
    private val repository: KoboldPresetRepository
) {
    operator fun invoke() = repository.subscribeToPresets()
}