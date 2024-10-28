package ru.kima.intelligentchat.domain.messaging.useCase

import ru.kima.intelligentchat.domain.messaging.repositoty.MessagingRepository

class SubscribeToMessagingStatus(
    private val repository: MessagingRepository
) {
    operator fun invoke() = repository.messagingStatus()
}