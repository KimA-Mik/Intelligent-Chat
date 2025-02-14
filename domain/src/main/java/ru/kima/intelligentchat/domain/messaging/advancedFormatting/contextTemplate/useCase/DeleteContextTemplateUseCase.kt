package ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.ContextTemplateRepository
import ru.kima.intelligentchat.domain.preferences.advancedFormatting.AdvancedFormattingRepository

class DeleteContextTemplateUseCase(
    private val advancedFormattingRepository: AdvancedFormattingRepository,
    private val contextTemplateRepository: ContextTemplateRepository
) {
    suspend operator fun invoke(id: Long): Boolean {
        val templates = contextTemplateRepository.subscribeToAll().first()
        val selectedTemplate = templates.find { it.id == id } ?: return false
        contextTemplateRepository.delete(selectedTemplate)

        templates.find { it.id != id }?.let {
            advancedFormattingRepository.updateSelectedContextTemplate(it.id)
        }

        return true
    }
}