package ru.kima.intelligentchat.domain.horde.useCase

import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository

class SelectActiveHordePresetUseCase(
    private val hordeStateRepository: HordeStateRepository,
) {
    suspend operator fun invoke(id: Long) {
        hordeStateRepository.selectPreset(id)
    }
}