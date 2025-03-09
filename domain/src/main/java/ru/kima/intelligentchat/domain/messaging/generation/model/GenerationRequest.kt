package ru.kima.intelligentchat.domain.messaging.generation.model

import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.InstructModeTemplate
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.tokenizer.Tokenizer

data class GenerationRequest(
    val maxResponseLength: Int,
    val maxContextLength: Int,
    val persona: Persona,
    val card: CharacterCard,
    val chat: FullChat,
    val stopSequence: List<String>,
    val generateFor: SenderType,
    val tokenizer: Tokenizer,
    val contextTemplate: ContextTemplate,
    val instructModeTemplate: InstructModeTemplate
)