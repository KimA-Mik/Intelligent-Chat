package ru.kima.intelligentchat.domain.messaging.repositoty

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.messaging.model.MessagingIndicator

interface MessagingRepository {
    fun messagingStatus(): Flow<MessagingIndicator>
    fun isGenerationAvailable(): Boolean
    fun initiateGeneration(chatId: Long, personaId: Long, senderType: SenderType)
    fun initiateSwipeGeneration(
        chatId: Long,
        messageId: Long,
        personaId: Long,
        senderType: SenderType
    )
    suspend fun cancelGeneration()
}