package ru.kima.intelligentchat.data.kobold.preset.mappers

import ru.kima.intelligentchat.data.kobold.preset.entities.KoboldPresetEntity
import ru.kima.intelligentchat.domain.presets.kobold.model.KoboldPreset

fun KoboldPresetEntity.toKoboldPreset(): KoboldPreset {
    return KoboldPreset(
        id = id,
        name = name,
        temperature = temperature,
        repetitionPenalty = repetitionPenalty,
        repetitionPenaltyRange = repetitionPenaltyRange,
        topP = topP,
        topA = topA,
        topK = topK,
        typical = typical,
        tailFreeSampling = tailFreeSampling,
        repetitionPenaltySlope = repetitionPenaltySlope,
        samplerOrder = samplerOrder,
        mirostat = mirostat,
        mirostatTau = mirostatTau,
        mirostatEta = mirostatEta,
        grammar = grammar,
    )
}

fun KoboldPreset.toEntity(): KoboldPresetEntity {
    return KoboldPresetEntity(
        id = id,
        name = name,
        temperature = temperature,
        repetitionPenalty = repetitionPenalty,
        repetitionPenaltyRange = repetitionPenaltyRange,
        topP = topP,
        topA = topA,
        topK = topK,
        typical = typical,
        tailFreeSampling = tailFreeSampling,
        repetitionPenaltySlope = repetitionPenaltySlope,
        samplerOrder = samplerOrder,
        mirostat = mirostat,
        mirostatTau = mirostatTau,
        mirostatEta = mirostatEta,
        grammar = grammar,
    )
}