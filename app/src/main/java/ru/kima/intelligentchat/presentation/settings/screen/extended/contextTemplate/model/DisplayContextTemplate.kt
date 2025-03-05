package ru.kima.intelligentchat.presentation.settings.screen.extended.contextTemplate.model

import androidx.compose.runtime.Immutable
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate

@Immutable
data class DisplayContextTemplate(
    val id: Long,
    val name: String,
    val storyString: String,
    val exampleSeparator: String,
    val chatStart: String
)

fun ContextTemplate.toDisplay() = DisplayContextTemplate(
    id = id,
    name = name,
    storyString = storyString,
    exampleSeparator = exampleSeparator,
    chatStart = chatStart
)

fun DisplayContextTemplate.toModel() = ContextTemplate(
    id = id,
    name = name,
    storyString = storyString,
    exampleSeparator = exampleSeparator,
    chatStart = chatStart
)