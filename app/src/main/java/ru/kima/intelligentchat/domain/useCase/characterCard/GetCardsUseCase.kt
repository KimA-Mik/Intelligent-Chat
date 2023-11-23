package ru.kima.intelligentchat.domain.useCase.characterCard

import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kima.intelligentchat.common.Resource
import ru.kima.intelligentchat.domain.repository.CharacterCardRepository

class GetCardsUseCase : KoinComponent {
    private val cardRepository: CharacterCardRepository by inject()

    operator fun invoke() = flow {
        emit(Resource.Loading())
        val result = cardRepository.getCharactersCards()
        emit(Resource.Success(result))
    }
}