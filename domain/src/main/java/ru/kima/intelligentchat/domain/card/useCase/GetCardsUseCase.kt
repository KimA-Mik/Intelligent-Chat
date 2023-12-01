package ru.kima.intelligentchat.domain.card.useCase

import kotlinx.coroutines.flow.flow
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class GetCardsUseCase(
    private val cardRepository: CharacterCardRepository
) {
    operator fun invoke() = flow {
        emit(Resource.Loading())
        val result = cardRepository.getCharactersCards()
        emit(Resource.Success(result))
    }
}