package ru.kima.intelligentchat.domain.card.useCase

import kotlinx.coroutines.flow.map
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.card.util.CardOrder

class GetCardsUseCase(
    private val cardRepository: CharacterCardRepository
) {
    operator fun invoke(filter: String = String(), order: CardOrder = CardOrder.Date) =
        cardRepository.getCharactersCards().map { cards ->
            var result = cards
            if (filter.isNotBlank()) {
                result = result.filter { it.name.contains(filter, ignoreCase = true) }
            }

            result = when (order) {
                CardOrder.Name -> result.sortedBy { it.name }
                CardOrder.Date -> result
            }
            result
        }
}
