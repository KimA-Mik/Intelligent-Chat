package ru.kima.intelligentchat.domain.koboldcpp

import ru.kima.intelligentchat.domain.messaging.generation.model.KoboldCppGenerationInput

interface KoboldCppRepository {
    suspend fun generate(
        baseUrl: String,
        input: KoboldCppGenerationInput
    ): Result<String>
}
