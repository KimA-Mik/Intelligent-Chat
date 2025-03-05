package ru.kima.intelligentchat.data.chat.advancedFormatting.contextTemplate

import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.model.ContextTemplate

fun ContextTemplate.toEntity() = ContextTemplateEntity(
    id = id,
    name = name,
    storyString = storyString,
    exampleSeparator = exampleSeparator,
    chatStart = chatStart
)

fun ContextTemplateEntity.toModel() = ContextTemplate(
    id = id,
    name = name,
    storyString = storyString,
    exampleSeparator = exampleSeparator,
    chatStart = chatStart
)