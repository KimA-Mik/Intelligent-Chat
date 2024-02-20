package ru.kima.intelligentchat.data.kobold.horde

import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.data.kobold.horde.mappers.toUserInfo
import ru.kima.intelligentchat.data.kobold.horde.model.RequestError
import ru.kima.intelligentchat.data.util.jsonConverterFactory.toConverterFactory
import ru.kima.intelligentchat.domain.horde.model.UserInfo
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository

class HordeRepositoryImpl(json: Json) : HordeRepository {
    private val api: HordeApi
    private val errorConverter: Converter<ResponseBody, RequestError>

    init {
        val contentType = MediaType.get("application/json")
        val retrofit = Retrofit.Builder()
            .baseUrl("https://stablehorde.net/api/v2/")
            .addConverterFactory(json.toConverterFactory(contentType))
            .build()

        api = retrofit.create()
        errorConverter =
            retrofit.responseBodyConverter(RequestError::class.java, emptyArray())
    }

    override suspend fun findUser(apiKey: String): Resource<UserInfo> {
        try {
            val response = api.findUser(apiKey)
            return if (response.isSuccessful) {
                val res = response.body()!!
                Resource.Success(res.toUserInfo())
            } else {
                val message = if (response.errorBody() != null) {
                    val error = errorConverter.convert(response.errorBody()!!)
                    "${error?.returnCode} - ${error?.message}"
                } else {
                    "Unknown error"
                }
                Resource.Error(message)
            }
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            return Resource.Error(message)
        }
    }
}