package ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.useCase

import ru.kima.intelligentchat.domain.preferences.advancedFormatting.AdvancedFormattingRepository

class SelectContextTemplateUseCase(
    private val repository: AdvancedFormattingRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.updateSelectedContextTemplate(id)
    }
}