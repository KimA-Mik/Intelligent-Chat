package ru.kima.intelligentchat.data.kobold.horde.mappers

import ru.kima.intelligentchat.data.kobold.horde.model.RequestAsyncDto
import ru.kima.intelligentchat.domain.horde.model.HordeAsyncRequest

fun RequestAsyncDto.toHordeAsyncRequest(): HordeAsyncRequest {
    return HordeAsyncRequest(
        id = id,
        kudos = kudos,
        message = message,
        warnings = warnings.map { it.message },
    )
}