package ru.kima.intelligentchat.domain.card.useCase

import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class DeleteAlternateGreetingUseCase(
    private val repository: CharacterCardRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteAlternateGreeting(id)
    }
}