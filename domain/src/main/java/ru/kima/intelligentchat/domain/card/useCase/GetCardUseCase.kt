package ru.kima.intelligentchat.domain.card.useCase

import kotlinx.coroutines.flow.flow
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class GetCardUseCase(
    private val cardRepository: CharacterCardRepository
) {
    operator fun invoke(id: Long) = flow {
        emit(Resource.Loading())
        val result = cardRepository.getCharacterCard(id)
        emit(Resource.Success(result))
    }
}