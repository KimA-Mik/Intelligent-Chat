package ru.kima.intelligentchat.presentation.android.preferences.hordeState.mapper

import ru.kima.intelligentchat.domain.preferences.horde.model.HordeModelInfo
import ru.kima.intelligentchat.presentation.android.preferences.hordeState.model.HordeModelInfoSchema

fun HordeModelInfoSchema.toModelInfo(): HordeModelInfo {
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

fun HordeModelInfo.toSchema(): HordeModelInfoSchema {
    return HordeModelInfoSchema(
        name = name,
        count = count,
        performance = performance,
        queued = queued,
        eta = eta,
        maxContextSize = maxContextSize,
        maxResponseLength = maxResponseLength,
    )
}