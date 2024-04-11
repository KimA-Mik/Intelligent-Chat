package ru.kima.intelligentchat.domain.card.useCase

import android.util.Log
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

private const val TAG = "UpdateCardUseCase"

class UpdateCardUseCase(
    private val cardRepository: CharacterCardRepository
) {
    suspend operator fun invoke(card: CharacterCard) {
        try {
            cardRepository.updateCharacterCard(card)
        } catch (e: Exception) {
            e.message?.let {
                Log.e(TAG, it)
            }
        }
    }
}