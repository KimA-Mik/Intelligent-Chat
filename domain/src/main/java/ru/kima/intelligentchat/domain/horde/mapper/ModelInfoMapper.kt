package ru.kima.intelligentchat.domain.horde.mapper

import ru.kima.intelligentchat.domain.horde.model.ModelInfo
import ru.kima.intelligentchat.domain.preferences.horde.model.HordeModelInfo

fun ModelInfo.toHordeModelInfo(): HordeModelInfo {
    return HordeModelInfo(
        name = name,
        count = count,
        performance = performance,
        queued = queued,
        eta = eta,
        maxContextSize = maxContextSize,
        maxResponseLength = maxResponseLength,
    )
}
