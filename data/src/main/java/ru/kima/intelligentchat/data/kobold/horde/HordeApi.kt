package ru.kima.intelligentchat.data.kobold.horde

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import ru.kima.intelligentchat.data.kobold.horde.model.ActiveModelDto
import ru.kima.intelligentchat.data.kobold.horde.model.UserDetailsDto

interface HordeApi {
    @GET("find_user")
    suspend fun findUser(@Header("apikey") apiKey: String): Response<UserDetailsDto>

    @GET("status/models")
    @Headers("type: text")
    suspend fun activeModels(): Response<List<ActiveModelDto>>
}