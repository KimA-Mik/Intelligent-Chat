package ru.kima.intelligentchat.domain.card.useCase

import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.card.util.getCardPhotoName
import ru.kima.intelligentchat.domain.image.useCase.DeleteImageUseCase

class DeleteCardUseCase(
    private val characterRepository: CharacterCardRepository,
    private val deleteImage: DeleteImageUseCase
) {
    suspend operator fun invoke(card: CharacterCard) {
        characterRepository.deleteCard(card.id)
        card.photoBytes?.let {
            val fileName = getCardPhotoName(card)
            deleteImage(fileName)
        }
    }
}