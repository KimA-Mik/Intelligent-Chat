package ru.kima.intelligentchat.domain.messaging.repositoty

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.messaging.model.GenerationStatus

interface MessagingRepository {
    fun messagingStatus(): Flow<GenerationStatus>
    fun initiateGeneration(chatId: Long, personaId: Long)
    fun cancelGeneration()
}