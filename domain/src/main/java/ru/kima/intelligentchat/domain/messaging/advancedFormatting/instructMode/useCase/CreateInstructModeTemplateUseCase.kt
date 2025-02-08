package ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.useCase

import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.InstructModeTemplateRepository
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.InstructModeTemplate

class CreateInstructModeTemplateUseCase(
    private val repository: InstructModeTemplateRepository
) {
    suspend operator fun invoke(name: String): Long {
        return repository.insert(InstructModeTemplate.default(name = name))
    }
}