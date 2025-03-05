package ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase

import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.ContextTemplateRepository
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate

class InsertContextTemplateUseCase(
    private val repository: ContextTemplateRepository
) {
    suspend operator fun invoke(contextTemplate: ContextTemplate): Long {
        val template = if (contextTemplate.id == 0L) {
            contextTemplate
        } else {
            contextTemplate.copy(id = 0L)
        }
        return repository.insert(template)
    }
}