package ru.kima.intelligentchat.domain.card.useCase

import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class PutCardUseCase(
    private val cardRepository: CharacterCardRepository
) {
    suspend operator fun invoke(card: CharacterCard) {
        cardRepository.putCharacterCard(card)
    }
}