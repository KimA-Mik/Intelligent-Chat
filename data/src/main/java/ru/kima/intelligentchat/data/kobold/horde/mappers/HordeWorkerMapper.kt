package ru.kima.intelligentchat.data.kobold.horde.mappers

import ru.kima.intelligentchat.data.kobold.horde.model.WorkerDto
import ru.kima.intelligentchat.domain.horde.model.HordeWorker

fun WorkerDto.toHordeWorker(): HordeWorker {
    return HordeWorker(
        id = id,
        maintenanceMode = maintenanceMode,
        maxContextLength = maxContextLength,
        maxLength = maxLength,
        models = models,
        name = name,
        nsfw = nsfw,
        online = online,
        trusted = trusted,
    )
}