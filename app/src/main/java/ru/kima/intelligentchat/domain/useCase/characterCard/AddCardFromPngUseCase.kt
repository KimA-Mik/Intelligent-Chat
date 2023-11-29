package ru.kima.intelligentchat.domain.useCase.characterCard

import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kima.intelligentchat.common.Resource
import ru.kima.intelligentchat.data.local.dataSource.ImageStorage
import ru.kima.intelligentchat.domain.common.PngBlockParser
import ru.kima.intelligentchat.domain.repository.CharacterCardRepository

class AddCardFromPngUseCase : KoinComponent {
    private val characterRepository: CharacterCardRepository by inject()
    private val updatePhoto: UpdateCardAvatarUseCase by inject()


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