package ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model

data class ContextTemplate(
    val id: Long,
    val name: String,
    val storyString: String,
    val exampleSeparator: String,
    val chatStart: String
) {
    companion object {
        fun default(
            id: Long = 0L,
            name: String = "",
            storyString: String = "",
            exampleSeparator: String = "",
            chatStart: String = ""
        ) = ContextTemplate(
            id = id,
            name = name,
            storyString = storyString,
            exampleSeparator = exampleSeparator,
            chatStart = chatStart
        )
    }
}

