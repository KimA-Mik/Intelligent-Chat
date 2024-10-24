package ru.kima.intelligentchat.data.kobold.horde

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.kima.intelligentchat.data.kobold.horde.model.ActiveModelDto
import ru.kima.intelligentchat.data.kobold.horde.model.GenerationInputDto
import ru.kima.intelligentchat.data.kobold.horde.model.HordeRequestStatusDto
import ru.kima.intelligentchat.data.kobold.horde.model.RequestAsyncDto
import ru.kima.intelligentchat.data.kobold.horde.model.UserDetailsDto
import ru.kima.intelligentchat.data.kobold.horde.model.WorkerDto

interface HordeApi {
    @GET("status/heartbeat")
    suspend fun heartbeat(): Response<Unit>

    @GET("find_user")
    suspend fun findUser(@Header("apikey") apiKey: String): Response<UserDetailsDto>

    @GET("status/models")
    suspend fun activeModels(@Query("type") type: String = "text"): Response<List<ActiveModelDto>>

    @GET("workers")
    suspend fun getWorkers(@Query("type") type: String = "text"): Response<List<WorkerDto>>

    @POST("generate/text/async")
    suspend fun generationRequest(
        @Header("apikey") apiKey: String,
        @Body generationInput: GenerationInputDto
    ): Response<RequestAsyncDto>

    @GET("generate/text/status/{id}")
    suspend fun getGenerationStatus(@Path("id") id: String): Response<HordeRequestStatusDto>

    @DELETE("generate/text/status/{id}")
    suspend fun cancelGenerationRequest(@Path("id") id: String): Response<HordeRequestStatusDto>
}