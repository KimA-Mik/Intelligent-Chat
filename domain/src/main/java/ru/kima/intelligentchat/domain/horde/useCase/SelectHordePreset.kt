package ru.kima.intelligentchat.domain.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordeState.HordeStateHandler

class SelectHordePreset(
    private val stateHandler: HordeStateHandler
) {
    suspend operator fun invoke(id: Long) {
        stateHandler.selectPreset(id)
    }
}