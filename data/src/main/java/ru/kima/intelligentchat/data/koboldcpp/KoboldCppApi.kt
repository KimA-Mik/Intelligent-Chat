package ru.kima.intelligentchat.data.koboldcpp

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.kima.intelligentchat.data.koboldcpp.model.KoboldCppGenerationInputDto
import ru.kima.intelligentchat.data.koboldcpp.model.KoboldCppGenerationResponseDto

interface KoboldCppApi {
    @POST("generate")
    suspend fun generate(
        @Body input: KoboldCppGenerationInputDto
    ): Response<KoboldCppGenerationResponseDto>
}
