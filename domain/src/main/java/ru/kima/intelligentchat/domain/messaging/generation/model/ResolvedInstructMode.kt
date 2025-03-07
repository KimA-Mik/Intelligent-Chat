package ru.kima.intelligentchat.domain.messaging.generation.model

data class ResolvedInstructMode(
    val userMessagePrefix: String,
    val userMessagePostfix: String,
    val assistantMessagePrefix: String,
    val assistantMessagePostfix: String,
    val firstAssistantPrefix: String,
    val lastAssistantPrefix: String,
    val firstUserPrefix: String,
    val lastUserPrefix: String
)

data class ResolvedInstructModeBudget(
    val userMessagePrefix: Int,
    val userMessagePostfix: Int,
    val assistantMessagePrefix: Int,
    val assistantMessagePostfix: Int,
    val firstAssistantPrefix: Int,
    val lastAssistantPrefix: Int,
    val firstUserPrefix: Int,
    val lastUserPrefix: Int
)