package ru.kima.intelligentchat.domain.card.useCase

import kotlinx.coroutines.flow.flow
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.card.util.CardOrder

class GetCardsUseCase(
    private val cardRepository: CharacterCardRepository
) {
    operator fun invoke(filter: String = String(), order: CardOrder = CardOrder.Date) = flow {
        emit(Resource.Loading())
        var result = cardRepository.getCharactersCards()

        if (filter.isNotBlank()) {
            result = result.filter { it.name.contains(filter, ignoreCase = true) }
        }

        result = when (order) {
            CardOrder.Name -> result.sortedBy { it.name }
            CardOrder.Date -> result
        }

        emit(Resource.Success(result))
    }
}