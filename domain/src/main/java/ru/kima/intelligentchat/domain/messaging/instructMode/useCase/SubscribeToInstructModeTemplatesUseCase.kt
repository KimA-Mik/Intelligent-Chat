package ru.kima.intelligentchat.domain.messaging.instructMode.useCase

import ru.kima.intelligentchat.domain.messaging.instructMode.InstructModeTemplateRepository

class SubscribeToInstructModeTemplatesUseCase(
    private val instructModeTemplateRepository: InstructModeTemplateRepository,
) {
    operator fun invoke() = instructModeTemplateRepository.subscribeToAll()
}