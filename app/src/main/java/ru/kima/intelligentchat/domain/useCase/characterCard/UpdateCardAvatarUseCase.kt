package ru.kima.intelligentchat.domain.useCase.characterCard

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kima.intelligentchat.common.Resource
import ru.kima.intelligentchat.domain.model.CharacterCard
import ru.kima.intelligentchat.domain.repository.CharacterCardRepository
import java.io.File

class UpdateCardAvatarUseCase : KoinComponent {
    private val characterRepository: CharacterCardRepository by inject()
    operator fun invoke(card: CharacterCard, uri: Uri) = flow {
        emit(Resource.Loading())
        val bitmap = BitmapFactory.decodeFile(uri.path)
        if (bitmap == null) {
            emit(Resource.Error("Could not load image"))
            return@flow
        }

        val outFile = File("avatars", "${card.id}.png")
        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, outFile.outputStream())) {
            emit(Resource.Error("Could not save image"))
            return@flow
        }

        val newCard = card.copy(photoFilePath = outFile.path)
        characterRepository.updateCharacterCard(newCard)
        emit(Resource.Success(newCard))
    }.flowOn(Dispatchers.IO)

}