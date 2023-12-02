package ru.kima.intelligentchat.domain.card.useCase

import kotlinx.coroutines.flow.flow
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class UpdateCardUseCase(
    private val cardRepository: CharacterCardRepository
) {

    operator fun invoke(card: CharacterCard) = flow<Resource<Boolean>> {
        emit(Resource.Loading())
        try {
            cardRepository.updateCharacterCard(card)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            e.message?.let {
                emit(Resource.Error(it))
            }
        }
    }
}