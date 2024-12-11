package ru.kima.intelligentchat.domain.messaging.instructMode.model

data class InstructModeTemplate(
    val id: Long,
    val name: String,
    val includeNamePolicy: IncludeNamePolicy,
    val wrapSequencesWithNewLine: Boolean,
    val userMessagePrefix: String,
    val userMessageSuffix: String,
    val assistantMessagePrefix: String,
    val assistantMessageSuffix: String,
    val systemSameAsUser: Boolean,
    val firstAssistantPrefix: String,
    val lastAssistantPrefix: String,
    val firstUserPrefix: String,
    val lastUserPrefix: String,
) {

    companion object {
        fun default(
            id: Long = 0,
            name: String = ""
        ) = InstructModeTemplate(
            id = id,
            name = name,
            includeNamePolicy = IncludeNamePolicy.ALWAYS,
            wrapSequencesWithNewLine = true,
            userMessagePrefix = "",
            userMessageSuffix = "",
            assistantMessagePrefix = "",
            assistantMessageSuffix = "",
            systemSameAsUser = false,
            firstAssistantPrefix = "",
            lastAssistantPrefix = "",
            firstUserPrefix = "",
            lastUserPrefix = ""
        )
    }
}