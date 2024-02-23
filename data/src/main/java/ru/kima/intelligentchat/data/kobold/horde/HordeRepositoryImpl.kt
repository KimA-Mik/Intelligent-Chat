package ru.kima.intelligentchat.data.kobold.horde

import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.data.kobold.horde.mappers.toActiveModel
import ru.kima.intelligentchat.data.kobold.horde.mappers.toUserInfo
import ru.kima.intelligentchat.data.kobold.horde.model.RequestError
import ru.kima.intelligentchat.data.util.jsonConverterFactory.toConverterFactory
import ru.kima.intelligentchat.domain.horde.model.ActiveModel
import ru.kima.intelligentchat.domain.horde.model.UserInfo
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository
import java.io.IOException

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
        return try {
            val response = api.findUser(apiKey)
            if (response.isSuccessful) {
                val res = response.body()!!
                Resource.Success(res.toUserInfo())
            } else {
                val code = response.code().toString()
                Resource.Error(code)
            }
        } catch (e: IOException) {
            Resource.Error("0")
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            Resource.Error(message)
        }
    }

    override suspend fun activeModels(): Resource<List<ActiveModel>> {
        return try {
            val response = api.activeModels()
            if (response.isSuccessful) {
                val res = response
                    .body()!!
                    .map { it.toActiveModel() }
                Resource.Success(res)
            } else {
                val message = getErrorMessage(response.errorBody())
                Resource.Error(message)
            }
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            Resource.Error(message)
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody?): String {
        return try {
            val error = errorConverter.convert(responseBody!!)
            error!!.message
        } catch (e: Exception) {
            "Unknown error"
        }
    }
}