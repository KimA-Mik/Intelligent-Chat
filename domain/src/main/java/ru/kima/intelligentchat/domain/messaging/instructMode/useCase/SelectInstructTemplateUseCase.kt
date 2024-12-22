package ru.kima.intelligentchat.domain.messaging.instructMode.useCase

import ru.kima.intelligentchat.domain.messaging.instructMode.InstructModeTemplateRepository
import ru.kima.intelligentchat.domain.messaging.instructMode.model.InstructModeTemplate
import ru.kima.intelligentchat.domain.preferences.advancedFormatting.AdvancedFormattingRepository

class SelectInstructTemplateUseCase(
    private val instructModeTemplateRepository: InstructModeTemplateRepository,
    private val advancedFormattingRepository: AdvancedFormattingRepository,
) {
    suspend operator fun invoke(id: Long): InstructModeTemplate? {
        val selected = instructModeTemplateRepository.getById(id)
        if (selected != null) {
            advancedFormattingRepository.updateSelectedInstructModeTemplate(id)
        }

        return selected
    }
}