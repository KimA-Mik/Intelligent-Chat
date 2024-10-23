package ru.kima.intelligentchat.domain.messaging.repositoty

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStatus

interface MessagingRepository {
    fun messagingStatus(): Flow<GenerationStatus>
    fun initiateGeneration(chatId: Long, personaId: Long, senderType: SenderType)
    fun cancelGeneration()
}