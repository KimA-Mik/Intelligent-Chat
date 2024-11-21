package ru.kima.intelligentchat.domain.card.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class RestoreCardUseCase(
    private val characterRepository: CharacterCardRepository,
) {
    suspend operator fun invoke(cardId: Long) {
        val card = characterRepository.getCharacterCard(cardId).first()
        characterRepository.updateCharacterCard(
            card.copy(
                deleted = false
            )
        )
    }
}