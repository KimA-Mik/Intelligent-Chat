package ru.kima.intelligentchat.domain.card.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.images.ImageStorage
import ru.kima.intelligentchat.domain.images.useCase.GetFreeImageNameUseCase

class UpdateCardAvatarUseCase(
    private val imageStorage: ImageStorage,
    private val getFreeImageName: GetFreeImageNameUseCase,
    private val characterRepository: CharacterCardRepository
) {
    suspend operator fun invoke(cardId: Long, bytes: ByteArray): String? {
        try {
            val card = characterRepository.getCharacterCard(cardId).first()

            val fileName = getFreeImageName()
            if (!imageStorage.saveImage(fileName, bytes)) return null
            characterRepository.updateCardAvatar(cardId, fileName)

            card.photoName?.let {
                imageStorage.deleteImage(it)
            }

            return fileName
        } catch (e: Exception) {
            e.message?.let {

            }
            return null
        }
    }
}