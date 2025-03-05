package ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.useCase

import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.InstructModeTemplateRepository
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.InstructModeTemplate

class UpdateInstructModeTemplateUseCase(
    private val repository: InstructModeTemplateRepository
) {
    suspend operator fun invoke(template: InstructModeTemplate) {
        repository.update(template)
    }
}