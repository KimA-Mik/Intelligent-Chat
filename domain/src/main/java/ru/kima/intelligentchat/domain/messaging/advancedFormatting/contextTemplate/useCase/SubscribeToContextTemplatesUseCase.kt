package ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase

import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.ContextTemplateRepository

class SubscribeToContextTemplatesUseCase(
    private val repository: ContextTemplateRepository
) {
    operator fun invoke() = repository.subscribeToAll()
}