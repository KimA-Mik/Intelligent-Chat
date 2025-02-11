package ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.ContextTemplateRepository
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate
import ru.kima.intelligentchat.domain.preferences.advancedFormatting.AdvancedFormattingRepository

class GetSelectedContextTemplateUseCase(
    private val advancedFormattingRepository: AdvancedFormattingRepository,
    private val contextTemplateRepository: ContextTemplateRepository
) {
    suspend operator fun invoke(): ContextTemplate {
        val selectedContextTemplateId =
            advancedFormattingRepository.preferences().first().selectedContextTemplate

        contextTemplateRepository.getById(selectedContextTemplateId)
            ?.let { return it }

        //TODO: Factor out `default`
        val newTemplate = ContextTemplate.default(name = "Default")
        val newId = contextTemplateRepository.insert(newTemplate)
        advancedFormattingRepository.updateSelectedContextTemplate(newId)
        return newTemplate.copy(id = newId)
    }
}