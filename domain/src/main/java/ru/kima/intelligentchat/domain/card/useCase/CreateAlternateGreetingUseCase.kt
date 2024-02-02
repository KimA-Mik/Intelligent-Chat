package ru.kima.intelligentchat.domain.card.useCase

import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class CreateAlternateGreetingUseCase(
    private val repository: CharacterCardRepository
) {
    suspend operator fun invoke(cardId: Long): Long {
        return repository.createAlternateGreeting(cardId)
    }
}