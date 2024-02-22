package ru.kima.intelligentchat.data.kobold.horde.mappers

import ru.kima.intelligentchat.data.kobold.horde.model.ActiveModelDto
import ru.kima.intelligentchat.domain.horde.model.ActiveModel

fun ActiveModelDto.toActiveModel() = ActiveModel(
    name = name,
    count = count,
    performance = performance,
    queued = queued,
    jobs = jobs,
    eta = eta,
    type = type
)