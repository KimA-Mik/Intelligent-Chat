package ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase

import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.ContextTemplateRepository
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate

class UpdateContextTemplateUseCase(
    private val repository: ContextTemplateRepository
) {
    suspend operator fun invoke(contextTemplate: ContextTemplate) {
        repository.update(contextTemplate)
    }
}