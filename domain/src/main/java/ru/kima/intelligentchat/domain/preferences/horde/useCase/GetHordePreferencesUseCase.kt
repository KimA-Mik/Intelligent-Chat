package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler

class GetHordePreferencesUseCase(private val handler: HordeStateHandler) {
    operator fun invoke() = handler.data
}
