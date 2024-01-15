package ru.kima.intelligentchat.domain.card.useCase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.card.util.CardOrder

class GetCardsListUseCase(
    cardRepository: CharacterCardRepository
) {
    private val query = MutableStateFlow("")
    private val order = MutableStateFlow(CardOrder.Date)
    private val personas = cardRepository.getCardsListEntries()

    fun filter(query: String) {
        this.query.value = query
    }

    fun order(order: CardOrder) {
        this.order.value = order
    }

    operator fun invoke() = combine(
        query,
        order,
        personas
    ) { query, order, personas ->
        var result = personas
        if (query.isNotBlank()) {
            result = result.filter { it.name.contains(query, ignoreCase = true) }
        }

        result = when (order) {
            CardOrder.Name -> result.sortedBy { it.name }
            CardOrder.Date -> result
        }

        return@combine result
    }
}