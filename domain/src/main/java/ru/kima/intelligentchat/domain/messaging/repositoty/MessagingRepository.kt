package ru.kima.intelligentchat.domain.messaging.repositoty

import kotlinx.coroutines.flow.Flow
import ru.kima.intelligentchat.domain.messaging.model.MessagingStatus

interface MessagingRepository {
    fun messagingStatus(): Flow<MessagingStatus>
    fun sendMessage()
    fun cancelMessage()
}