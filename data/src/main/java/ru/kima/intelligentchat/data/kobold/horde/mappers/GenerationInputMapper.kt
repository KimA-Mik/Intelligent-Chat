package ru.kima.intelligentchat.data.kobold.horde.mappers

import ru.kima.intelligentchat.data.kobold.horde.model.GenerationInputDto
import ru.kima.intelligentchat.domain.horde.model.GenerationInput

fun GenerationInput.toDto(): GenerationInputDto {
    return GenerationInputDto(
        models = models,
        params = params.toDto(),
        prompt = prompt,
        trustedWorkers = trustedWorkers,
    )
}