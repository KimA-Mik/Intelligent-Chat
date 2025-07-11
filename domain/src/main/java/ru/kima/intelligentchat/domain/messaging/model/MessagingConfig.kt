package ru.kima.intelligentchat.domain.messaging.model

import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.InstructModeTemplate
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationRequest
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.tokenizer.Tokenizer

data class MessagingConfig(
    val maxResponseLength: Int,
    val maxContextLength: Int,
    val stopSequence: List<String>,
    val contextTemplate: ContextTemplate,
    val instructModeTemplate: InstructModeTemplate
) {
    fun constructGenerationRequest(
        persona: Persona,
        card: CharacterCard,
        chat: FullChat,
        generateFor: SenderType,
        tokenizer: Tokenizer,
    ) = GenerationRequest(
        maxResponseLength = maxResponseLength,
        maxContextLength = maxContextLength,
        persona = persona,
        card = card,
        chat = chat,
        stopSequence = stopSequence,
        generateFor = generateFor,
        tokenizer = tokenizer,
        contextTemplate = contextTemplate,
        instructModeTemplate = instructModeTemplate
    )
}
