package ru.kima.intelligentchat.domain.card.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class DeleteCardUseCase(
    private val characterRepository: CharacterCardRepository,
) {
    suspend operator fun invoke(card: CharacterCard) {
        characterRepository.updateCharacterCard(
            card.copy(
                deleted = true
            )
        )
    }

    suspend operator fun invoke(cardId: Long) {
        val card = characterRepository.getCharacterCard(cardId).first()
        characterRepository.updateCharacterCard(
            card.copy(
                deleted = true
            )
        )
    }
}