package ru.kima.intelligentchat.domain.preferences.useCase

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.core.preferences.AppPreferences
import ru.kima.intelligentchat.core.preferences.PreferencesHandler

class GetPreferencesUseCase(private val preferences: PreferencesHandler) {
    operator fun invoke(): Flow<AppPreferences> {
        return preferences.data
    }
}