package ru.kima.intelligentchat.data.chat.mappers

import ru.kima.intelligentchat.data.chat.types.sender.SenderTypeDto
import ru.kima.intelligentchat.domain.chat.model.SenderType

fun SenderTypeDto.toSenderType(): SenderType {
    return when (this) {
        SenderTypeDto.Character -> SenderType.Character
        SenderTypeDto.Persona -> SenderType.Persona
    }
}

fun SenderType.toDto(): SenderTypeDto {
    return when (this) {
        SenderType.Character -> SenderTypeDto.Character
        SenderType.Persona -> SenderTypeDto.Persona
    }
}