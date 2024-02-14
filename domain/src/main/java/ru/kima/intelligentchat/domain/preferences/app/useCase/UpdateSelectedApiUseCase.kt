package ru.kima.intelligentchat.domain.preferences.app.useCase

import ru.kima.intelligentchat.core.common.API_TYPE
import ru.kima.intelligentchat.core.preferences.appPreferences.PreferencesHandler

class UpdateSelectedApiUseCase(private val preferencesHandler: PreferencesHandler) {
    suspend operator fun invoke(apiType: API_TYPE) {
        preferencesHandler.updateSelectedAip(apiType)
    }
}