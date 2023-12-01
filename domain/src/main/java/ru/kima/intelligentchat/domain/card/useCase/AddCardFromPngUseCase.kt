package ru.kima.intelligentchat.domain.card.useCase

import kotlinx.coroutines.flow.flow
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.core.utils.PngBlockParser
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class AddCardFromPngUseCase(
    private val characterRepository: CharacterCardRepository,
    private val updatePhoto: UpdateCardAvatarUseCase
) {


    operator fun invoke(imageBytes: ByteArray) = flow<Resource<Long>> {
        emit(Resource.Loading())
        try {
            val parser = PngBlockParser()
            val data = parser.getTextBlock(imageBytes)
            val id = characterRepository.putCharacterCardFromJson(data)
            val newCard = characterRepository.getCharacterCard(id)
            //TODO: it feels ugly, redo later
            updatePhoto.invoke(newCard, imageBytes).collect { resource ->
                when (resource) {
                    is Resource.Error -> emit(Resource.Error(resource.message!!))
                    is Resource.Loading -> {}
                    is Resource.Success -> emit(Resource.Success(resource.data!!.id))
                }
            }
        }
        //TODO: Improve error handling
        catch (e: Exception) {
            e.message?.let {
                emit(Resource.Error(it))
            }
        }
    }
}