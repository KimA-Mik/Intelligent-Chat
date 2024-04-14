package ru.kima.intelligentchat.data.kobold.horde.mappers

import ru.kima.intelligentchat.data.kobold.horde.model.HordeParamsDto
import ru.kima.intelligentchat.domain.horde.model.HordeGenerationParams

fun HordeGenerationParams.toDto(): HordeParamsDto {
    return HordeParamsDto(
        canAbort = canAbort,
        frmtadsnsp = frmtadsnsp,
        frmtrmblln = frmtrmblln,
        frmtrmspch = frmtrmspch,
        frmttriminc = frmttriminc,
        grammar = grammar,
        guiSettings = guiSettings,
        maxContextLength = maxContextLength,
        maxLength = maxLength,
        minP = minP,
        mirostat = mirostat,
        mirostatEta = mirostatEta,
        mirostatTau = mirostatTau,
        n = n,
        repPen = repPen,
        repetitionPenaltyRange = repetitionPenaltyRange,
        repetitionPenaltySlope = repetitionPenaltySlope,
        samplerOrder = samplerOrder,
        singleLine = singleLine,
        stopSequence = stopSequence,
        streaming = streaming,
        temperature = temperature,
        tailFreeSampling = tailFreeSampling,
        topA = topA,
        topK = topK,
        topP = topP,
        typical = typical,
        useDefaultBadWordsIds = useDefaultBadWordsIds,
        useWorldInfo = useWorldInfo,
    )
}