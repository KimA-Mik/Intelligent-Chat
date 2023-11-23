package ru.kima.intelligentchat.domain.useCase.characterCard

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kima.intelligentchat.domain.model.CharacterCard
import ru.kima.intelligentchat.domain.repository.CharacterCardRepository

class PutCardUseCase : KoinComponent {
    private val cardRepository: CharacterCardRepository by inject()

    suspend operator fun invoke(card: CharacterCard) {
        cardRepository.putCharacterCard(card)
    }
}