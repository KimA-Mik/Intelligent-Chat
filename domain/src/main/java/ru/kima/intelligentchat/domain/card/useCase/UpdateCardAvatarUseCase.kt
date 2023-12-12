package ru.kima.intelligentchat.domain.card.useCase

import kotlinx.coroutines.flow.flow
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.core.common.Resource.Error
import ru.kima.intelligentchat.core.common.Resource.Loading
import ru.kima.intelligentchat.core.common.Resource.Success
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository

class UpdateCardAvatarUseCase(
    private val characterRepository: CharacterCardRepository,
) {
    operator fun invoke(card: CharacterCard, bytes: ByteArray) = flow<Resource<CharacterCard>> {
        try {
            emit(Loading())
            emit(Success(characterRepository.updateCardAvatar(card, bytes)))
        } catch (e: Exception) {
            e.message?.let {
                emit(Error(it))
            }
        }
    }
}