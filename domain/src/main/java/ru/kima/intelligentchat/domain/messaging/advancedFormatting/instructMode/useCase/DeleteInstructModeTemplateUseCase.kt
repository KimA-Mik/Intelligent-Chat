package ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.InstructModeTemplateRepository
import ru.kima.intelligentchat.domain.preferences.advancedFormatting.AdvancedFormattingRepository

class DeleteInstructModeTemplateUseCase(
    private val instructModeTemplateRepository: InstructModeTemplateRepository,
    private val advancedFormattingRepository: AdvancedFormattingRepository,
) {
    suspend operator fun invoke(id: Long): Boolean {
        val templates = instructModeTemplateRepository.subscribeToAll().first()
        val deletable = templates.find { it.id == id } ?: return false
        instructModeTemplateRepository.delete(deletable)

        val selectedInstructModeTemplate = advancedFormattingRepository
            .preferences().first().selectedInstructModeTemplate
        if (selectedInstructModeTemplate != id) return true
        val newId = templates.firstOrNull { it.id != id }?.id ?: 0L
        advancedFormattingRepository.updateSelectedInstructModeTemplate(newId)

        return true
    }
}