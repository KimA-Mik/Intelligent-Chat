package ru.kima.intelligentchat.data.kobold.horde.mappers

import ru.kima.intelligentchat.data.kobold.horde.model.GenerationDto
import ru.kima.intelligentchat.data.kobold.horde.model.GenerationMetadataDto
import ru.kima.intelligentchat.data.kobold.horde.model.HordeRequestStatusDto
import ru.kima.intelligentchat.domain.horde.model.HordeGeneration
import ru.kima.intelligentchat.domain.horde.model.HordeGenerationMetadata
import ru.kima.intelligentchat.domain.horde.model.HordeRequestStatus


fun HordeRequestStatusDto.toHordeRequestStatus(): HordeRequestStatus {
    return HordeRequestStatus(
        done = done,
        faulted = faulted,
        finished = finished,
        generations = generations.map { it.toHordeGeneration() },
        isPossible = isPossible,
        kudos = kudos,
        processing = processing,
        queuePosition = queuePosition,
        restarted = restarted,
        waitTime = waitTime,
        waiting = waiting,
    )
}

fun GenerationDto.toHordeGeneration(): HordeGeneration {
    return HordeGeneration(
        metadata = metadata.map { it.toHordeGenerationMetadata() },
        model = model,
        seed = seed,
        state = state,
        text = text,
        workerId = workerId,
        workerName = workerName,
    )
}

fun GenerationMetadataDto.toHordeGenerationMetadata(): HordeGenerationMetadata {
    return HordeGenerationMetadata(
        ref = ref,
        type = type,
        value = value,
    )
}