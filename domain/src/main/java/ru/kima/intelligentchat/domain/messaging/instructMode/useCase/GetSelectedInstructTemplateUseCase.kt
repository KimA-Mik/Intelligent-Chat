package ru.kima.intelligentchat.domain.messaging.instructMode.useCase

import kotlinx.coroutines.flow.first
import ru.kima.intelligentchat.domain.messaging.instructMode.InstructModeTemplateRepository
import ru.kima.intelligentchat.domain.messaging.instructMode.model.InstructModeTemplate
import ru.kima.intelligentchat.domain.preferences.advancedFormatting.AdvancedFormattingRepository

class GetSelectedInstructTemplateUseCase(
    private val instructModeTemplateRepository: InstructModeTemplateRepository,
    private val advancedFormattingRepository: AdvancedFormattingRepository,
) {
    suspend operator fun invoke(): InstructModeTemplate {
        val selectedInstructModeTemplate =
            advancedFormattingRepository.preferences().first().selectedInstructModeTemplate

        instructModeTemplateRepository.getById(selectedInstructModeTemplate)?.let {
            return it
        }

        val defaultId = insertDefault()
        advancedFormattingRepository.updateSelectedInstructModeTemplate(defaultId)
        return instructModeTemplateRepository.getById(defaultId)!!
    }

    private suspend fun insertDefault(): Long {
        val defaultTemplate = InstructModeTemplate.default()
        return instructModeTemplateRepository.insert(defaultTemplate)
    }
}