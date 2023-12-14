package ru.kima.intelligentchat.domain.card.useCase

import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class GetCardUseCase(
    private val cardRepository: CharacterCardRepository
) {
    operator fun invoke(id: Long) = cardRepository.getCharacterCard(id)

}