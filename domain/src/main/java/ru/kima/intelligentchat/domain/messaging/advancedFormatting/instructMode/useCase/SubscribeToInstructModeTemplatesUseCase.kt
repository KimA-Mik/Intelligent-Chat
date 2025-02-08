package ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.useCase

import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.InstructModeTemplateRepository

class SubscribeToInstructModeTemplatesUseCase(
    private val instructModeTemplateRepository: InstructModeTemplateRepository,
) {
    operator fun invoke() = instructModeTemplateRepository.subscribeToAll()
}