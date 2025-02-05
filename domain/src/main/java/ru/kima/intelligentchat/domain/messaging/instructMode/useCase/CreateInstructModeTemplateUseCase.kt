package ru.kima.intelligentchat.domain.messaging.instructMode.useCase

import ru.kima.intelligentchat.domain.messaging.instructMode.InstructModeTemplateRepository
import ru.kima.intelligentchat.domain.messaging.instructMode.model.InstructModeTemplate

class CreateInstructModeTemplateUseCase(
    private val repository: InstructModeTemplateRepository
) {
    suspend operator fun invoke(name: String): Long {
        return repository.insert(InstructModeTemplate.default(name = name))
    }
}