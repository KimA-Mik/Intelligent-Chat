package ru.kima.intelligentchat.domain.preferences.textCompletion.useCase

import ru.kima.intelligentchat.domain.preferences.textCompletion.TextCompletionPreferencesRepository

class GetTextCompletionPreferencesUseCase(
    private val repository: TextCompletionPreferencesRepository
) {
    operator fun invoke() = repository.preferences()
}
