package ru.kima.intelligentchat.domain.card.useCase

import ru.kima.intelligentchat.domain.card.model.AltGreeting
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class UpdateAlternateGreetingUseCase(
    private val repository: CharacterCardRepository
) {
    suspend operator fun invoke(greeting: AltGreeting, cardId: Long) {
        repository.updateAlternateGreeting(greeting, cardId)
    }
}