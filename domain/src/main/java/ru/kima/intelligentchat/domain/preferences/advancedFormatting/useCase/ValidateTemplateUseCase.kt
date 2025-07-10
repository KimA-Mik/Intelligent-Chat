package ru.kima.intelligentchat.domain.preferences.advancedFormatting.useCase

import ru.kima.intelligentchat.domain.messaging.generation.prompting.TemplateResolver

class ValidateTemplateUseCase(
    private val templateResolver: TemplateResolver
) {
    operator fun invoke(template: String): Result {
        return try {
            templateResolver.constructFullTemplate(template, mockInputData)
            Result.Success
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: e.message ?: e.toString())
        }
    }

    private val mockInputData = TemplateResolver.FullInputData(
        system = "system",
        user = "user",
        persona = "persona",
        char = "char",
        personality = "personality",
        description = "description",
        scenario = "scenario"
    )

    sealed interface Result {
        data object Success : Result
        data class Error(val message: String) : Result
    }
}