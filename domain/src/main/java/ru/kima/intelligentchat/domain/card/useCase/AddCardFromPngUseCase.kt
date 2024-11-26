package ru.kima.intelligentchat.domain.card.useCase

import kotlinx.coroutines.flow.flow
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.common.Resource
import ru.kima.intelligentchat.domain.utils.PngBlockParser

class AddCardFromPngUseCase(
    private val characterRepository: CharacterCardRepository,
    private val updatePhoto: UpdateCardAvatarUseCase
) {
    operator fun invoke(imageBytes: ByteArray) = flow<Resource<Long>> {
        try {
            val parser = PngBlockParser()
            val data = parser.getTextBlock(imageBytes)
            val id = characterRepository.putCharacterCardFromJson(data)
            updatePhoto(id, imageBytes)
            emit(Resource.Success(id))
        }
        //TODO: Improve error handling
        catch (e: Exception) {
            e.message?.let {
                emit(Resource.Error(it))
            }
        }
    }
}