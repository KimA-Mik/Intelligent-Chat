package ru.kima.intelligentchat.domain.useCase.characterCard

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kima.intelligentchat.common.Resource
import ru.kima.intelligentchat.data.local.dataSource.ImageStorage
import ru.kima.intelligentchat.domain.model.CharacterCard
import ru.kima.intelligentchat.domain.repository.CharacterCardRepository
import java.io.ByteArrayOutputStream

class UpdateCardAvatarUseCase : KoinComponent {
    private val characterRepository: CharacterCardRepository by inject()
    private val imageStorage: ImageStorage by inject()
    operator fun invoke(card: CharacterCard, bytes: ByteArray) = flow {
        emit(Resource.Loading())
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        if (bitmap == null) {
            emit(Resource.Error("Could not load image"))
            return@flow
        }

        val fileName = "avatar-${card.id}.png"

        val outputStream = ByteArrayOutputStream()
        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
            emit(Resource.Error("Could not save image"))
            return@flow
        }

        val photoBytes = outputStream.toByteArray()
        imageStorage.saveImage(fileName, photoBytes)
        val newCard = card.copy(photoBytes = photoBytes)
        characterRepository.updateCharacterCard(newCard)
        emit(Resource.Success(newCard))
    }
}