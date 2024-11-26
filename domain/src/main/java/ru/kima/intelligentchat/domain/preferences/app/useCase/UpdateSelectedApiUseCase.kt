package ru.kima.intelligentchat.domain.preferences.app.useCase

import ru.kima.intelligentchat.domain.common.ApiType
import ru.kima.intelligentchat.domain.preferences.app.AppPreferencesRepository

class UpdateSelectedApiUseCase(
    private val repository: AppPreferencesRepository
) {
    suspend operator fun invoke(apiType: ApiType) {
        repository.updateSelectedAip(apiType)
    }
}