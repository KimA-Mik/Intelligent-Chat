package ru.kima.intelligentchat.domain.card.useCase

import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class UpdateCardAvatarUseCase(
    private val characterRepository: CharacterCardRepository,
) {
    suspend operator fun invoke(cardId: Long, bytes: ByteArray) {
        try {
            characterRepository.updateCardAvatar(cardId, bytes)
        } catch (e: Exception) {
            e.message?.let {

            }
        }
    }
}