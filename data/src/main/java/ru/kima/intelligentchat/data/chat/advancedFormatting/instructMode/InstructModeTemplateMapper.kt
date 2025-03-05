package ru.kima.intelligentchat.data.chat.advancedFormatting.instructMode

import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.IncludeNamePolicy
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.InstructModeTemplate

fun InstructModeTemplate.toEntity() = InstructModeTemplateEntity(
    id = id,
    name = name,
    includeNamePolicy = includeNamePolicy.toDto(),
    wrapSequencesWithNewLine = wrapSequencesWithNewLine,
    userMessagePrefix = userMessagePrefix,
    userMessagePostfix = userMessagePostfix,
    assistantMessagePrefix = assistantMessagePrefix,
    assistantMessagePostfix = assistantMessagePostfix,
    systemSameAsUser = systemSameAsUser,
    firstAssistantPrefix = firstAssistantPrefix,
    lastAssistantPrefix = lastAssistantPrefix,
    firstUserPrefix = firstUserPrefix,
    lastUserPrefix = lastUserPrefix,
)

fun InstructModeTemplateEntity.toDomain() = InstructModeTemplate(
    id = id,
    name = name,
    includeNamePolicy = includeNamePolicy.toDomain(),
    wrapSequencesWithNewLine = wrapSequencesWithNewLine,
    userMessagePrefix = userMessagePrefix,
    userMessagePostfix = userMessagePostfix,
    assistantMessagePrefix = assistantMessagePrefix,
    assistantMessagePostfix = assistantMessagePostfix,
    systemSameAsUser = systemSameAsUser,
    firstAssistantPrefix = firstAssistantPrefix,
    lastAssistantPrefix = lastAssistantPrefix,
    firstUserPrefix = firstUserPrefix,
    lastUserPrefix = lastUserPrefix
)

fun IncludeNamePolicy.toDto() = when (this) {
    IncludeNamePolicy.NEVER -> IncludeNamePolicyDto.NEVER
    IncludeNamePolicy.ALWAYS -> IncludeNamePolicyDto.ALWAYS
}

fun IncludeNamePolicyDto.toDomain() = when (this) {
    IncludeNamePolicyDto.NEVER -> IncludeNamePolicy.NEVER
    IncludeNamePolicyDto.ALWAYS -> IncludeNamePolicy.ALWAYS
}
