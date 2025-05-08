package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate.model

import io.mcarle.konvert.api.KonvertFrom
import io.mcarle.konvert.api.KonvertTo
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.IncludeNamePolicy
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.model.InstructModeTemplate

@KonvertTo(
    InstructModeTemplate::class,
    mapFunctionName = "toModel"
)
data class DisplayInstructModeTemplate(
    val id: Long,
    val name: String,
    val includeNamePolicy: IncludeNamePolicy,
    val wrapSequencesWithNewLine: Boolean,
    val userMessagePrefix: String,
    val userMessagePostfix: String,
    val assistantMessagePrefix: String,
    val assistantMessagePostfix: String,
    val systemSameAsUser: Boolean,
    val firstAssistantPrefix: String,
    val lastAssistantPrefix: String,
    val firstUserPrefix: String,
    val lastUserPrefix: String,
) {
    @KonvertFrom(
        InstructModeTemplate::class,
        mapFunctionName = "fromModel"
    )
    companion object
}

//fun InstructModeTemplate.toDisplay() =
//    DisplayInstructModeTemplate(
//        id = id,
//        name = name,
//        includeNamePolicy = includeNamePolicy,
//        wrapSequencesWithNewLine = wrapSequencesWithNewLine,
//        userMessagePrefix = userMessagePrefix,
//        userMessagePostfix = userMessagePostfix,
//        assistantMessagePrefix = assistantMessagePrefix,
//        assistantMessagePostfix = assistantMessagePostfix,
//        systemSameAsUser = systemSameAsUser,
//        firstAssistantPrefix = firstAssistantPrefix,
//        lastAssistantPrefix = lastAssistantPrefix,
//        firstUserPrefix = firstUserPrefix,
//        lastUserPrefix = lastUserPrefix
//    )

//fun DisplayInstructModeTemplate.toModel() =
//    InstructModeTemplate(
//        id = id,
//        name = name,
//        includeNamePolicy = includeNamePolicy,
//        wrapSequencesWithNewLine = wrapSequencesWithNewLine,
//        userMessagePrefix = userMessagePrefix,
//        userMessagePostfix = userMessagePostfix,
//        assistantMessagePrefix = assistantMessagePrefix,
//        assistantMessagePostfix = assistantMessagePostfix,
//        systemSameAsUser = systemSameAsUser,
//        firstAssistantPrefix = firstAssistantPrefix,
//        lastAssistantPrefix = lastAssistantPrefix,
//        firstUserPrefix = firstUserPrefix,
//        lastUserPrefix = lastUserPrefix
//    )