package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository

class GetHordePreferencesUseCase(
    private val repository: HordeStateRepository
) {
    operator fun invoke() = repository.hordeState()
}
