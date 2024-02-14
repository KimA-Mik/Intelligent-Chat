package ru.kima.intelligentchat.domain.preferences.horde.useCase

import ru.kima.intelligentchat.core.preferences.hordePreferences.HordePreferencesHandler

class GetHordePreferencesUseCase(private val handler: HordePreferencesHandler) {
    operator fun invoke() = handler.data
}
