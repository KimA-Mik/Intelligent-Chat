package ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model

data class ContextTemplate(
    val id: Long,
    val name: String,
    val storyString: String,
    val exampleSeparator: String,
    val chatStart: String
)
