package ru.kima.intelligentchat.domain.messaging.useCase

import ru.kima.intelligentchat.domain.messaging.repositoty.MessagingRepository

class CancelMessageUseCase(
    private val messagingRepository: MessagingRepository,
) {
    suspend operator fun invoke() {
        messagingRepository.cancelGeneration()
    }
}