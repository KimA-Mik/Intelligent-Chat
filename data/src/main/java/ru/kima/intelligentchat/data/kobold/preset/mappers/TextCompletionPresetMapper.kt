package ru.kima.intelligentchat.data.kobold.preset.mappers

import ru.kima.intelligentchat.data.kobold.preset.entities.KoboldPresetEntity
import ru.kima.intelligentchat.data.kobold.preset.entities.TextCompletionPresetEntity
import ru.kima.intelligentchat.domain.presets.kobold.model.KoboldPreset

fun KoboldPresetEntity.toTextCompletionPresetEntity() = TextCompletionPresetEntity(
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
    minP = minP,
    grammar = grammar
)

fun TextCompletionPresetEntity.toKoboldPreset() = KoboldPreset(
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
    minP = minP,
    grammar = grammar
)

fun KoboldPreset.toTextCompletionPresetEntity() = TextCompletionPresetEntity(
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
    minP = minP,
    grammar = grammar
)
