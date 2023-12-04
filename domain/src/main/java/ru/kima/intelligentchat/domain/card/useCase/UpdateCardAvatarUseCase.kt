package ru.kima.intelligentchat.domain.card.useCase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.flow.flow
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.card.util.getCardPhotoName
import ru.kima.intelligentchat.domain.image.useCase.SaveImageUseCase
import java.io.ByteArrayOutputStream

class UpdateCardAvatarUseCase(
    private val characterRepository: CharacterCardRepository,
    private val saveImage: SaveImageUseCase
) {
    operator fun invoke(card: CharacterCard, bytes: ByteArray) = flow {
        emit(Resource.Loading())
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        if (bitmap == null) {
            emit(Resource.Error("Could not load image"))
            return@flow
        }

        val fileName = getCardPhotoName(card)

        val outputStream = ByteArrayOutputStream()
        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
            emit(Resource.Error("Could not save image"))
            return@flow
        }

        val photoBytes = outputStream.toByteArray()
        val photo = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size)
        saveImage(fileName, photoBytes)
        val newCard = card.copy(photoBytes = photo)
        characterRepository.updateCharacterCard(newCard)
        emit(Resource.Success(newCard))
    }
}