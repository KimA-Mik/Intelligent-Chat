package ru.kima.intelligentchat.domain.preferences.app.useCase

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.core.preferences.appPreferences.AppPreferences
import ru.kima.intelligentchat.core.preferences.appPreferences.PreferencesHandler

class GetPreferencesUseCase(private val preferences: PreferencesHandler) {
    operator fun invoke(): Flow<AppPreferences> {
        return preferences.data
    }
}