package ru.kima.intelligentchat.domain.messaging.instructMode.useCase

import ru.kima.intelligentchat.domain.messaging.instructMode.InstructModeTemplateRepository
import ru.kima.intelligentchat.domain.messaging.instructMode.model.InstructModeTemplate

class UpdateInstructModeTemplateUseCase(
    private val repository: InstructModeTemplateRepository
) {
    suspend operator fun invoke(template: InstructModeTemplate) {
        repository.update(template)
    }
}