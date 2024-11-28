package ru.kima.intelligentchat.domain.preferences.app.useCase

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.preferences.app.AppPreferences
import ru.kima.intelligentchat.domain.preferences.app.AppPreferencesRepository

class GetPreferencesUseCase(
    private val repository: AppPreferencesRepository
) {
    operator fun invoke(): Flow<AppPreferences> {
        return repository.preferences()
    }
}