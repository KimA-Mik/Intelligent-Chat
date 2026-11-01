package ru.kima.intelligentchat.domain.preferences.textCompletion.useCase

import ru.kima.intelligentchat.domain.preferences.textCompletion.TextCompletionPreferencesRepository

class UpdateTextCompletionBaseUrlUseCase(
    private val repository: TextCompletionPreferencesRepository
) {
    suspend operator fun invoke(baseUrl: String) = repository.updateBaseUrl(baseUrl)
}
