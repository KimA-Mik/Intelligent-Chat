package ru.kima.intelligentchat.domain.card.useCase

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
}