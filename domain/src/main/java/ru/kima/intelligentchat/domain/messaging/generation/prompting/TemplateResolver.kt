package ru.kima.intelligentchat.domain.messaging.generation.prompting

import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.persona.model.Persona

interface TemplateResolver {
    fun constructBasicTemplate(template: String, inputData: BasicInputData): String
    fun constructFullTemplate(template: String, inputData: FullInputData): String

    data class BasicInputData(
        val user: String,
        val char: String
    )

    data class FullInputData(
        val system: String?,
        val user: String,
        val persona: String?,
        val char: String,
        val description: String?,
        val scenario: String?,
    ) {
        companion object {
            fun construct(persona: Persona, card: CharacterCard): FullInputData {
                return FullInputData(
                    system = card.systemPrompt.ifBlank { null },
                    user = persona.name,
                    persona = persona.description.ifBlank { null },
                    char = card.name,
                    description = card.description.ifBlank { null },
                    scenario = card.scenario.ifBlank { null },
                )
            }
        }
    }
}