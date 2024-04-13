package ru.kima.intelligentchat.data.kobold.horde

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.data.kobold.horde.mappers.toActiveModel
import ru.kima.intelligentchat.data.kobold.horde.mappers.toDto
import ru.kima.intelligentchat.data.kobold.horde.mappers.toHordeAsyncRequest
import ru.kima.intelligentchat.data.kobold.horde.mappers.toHordeRequestStatus
import ru.kima.intelligentchat.data.kobold.horde.mappers.toHordeWorker
import ru.kima.intelligentchat.data.kobold.horde.mappers.toUserInfo
import ru.kima.intelligentchat.data.kobold.horde.model.ConnectionState
import ru.kima.intelligentchat.data.kobold.horde.model.RequestError
import ru.kima.intelligentchat.data.kobold.horde.model.RequestValidationError
import ru.kima.intelligentchat.data.kobold.horde.model.WorkerDto
import ru.kima.intelligentchat.data.util.jsonConverterFactory.toConverterFactory
import ru.kima.intelligentchat.domain.horde.model.ActiveModel
import ru.kima.intelligentchat.domain.horde.model.GenerationInput
import ru.kima.intelligentchat.domain.horde.model.HordeAsyncRequest
import ru.kima.intelligentchat.domain.horde.model.HordeRequestStatus
import ru.kima.intelligentchat.domain.horde.model.HordeWorker
import ru.kima.intelligentchat.domain.horde.model.UserInfo
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository
import java.io.IOException

class HordeRepositoryImpl(json: Json) : HordeRepository {
    private val api: HordeApi
    private val errorConverter: Converter<ResponseBody, RequestError>
    private val requestValidationErrorConverter: Converter<ResponseBody, RequestValidationError>

    init {
        val contentType = MediaType.get("application/json")
        val retrofit = Retrofit.Builder()
            .baseUrl("https://stablehorde.net/api/v2/")
            .addConverterFactory(json.toConverterFactory(contentType))
            .build()

        api = retrofit.create()
        errorConverter =
            retrofit.responseBodyConverter(RequestError::class.java, emptyArray())
        requestValidationErrorConverter =
            retrofit.responseBodyConverter(RequestValidationError::class.java, emptyArray())
    }

    override suspend fun heartbeat(): Resource<Unit> {
        return try {
            val response = api.heartbeat()
            if (response.isSuccessful) {
                ConnectionState.isConnected.value = true
                Resource.Success(Unit)
            } else {
                Resource.Error("The heart of horde doesn't beat for some reason.")
            }
        } catch (e: IOException) {
            ConnectionState.isConnected.value = false
            Resource.Error("0")
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            Resource.Error(message)
        }
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
            ConnectionState.isConnected.value = false
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
        } catch (e: IOException) {
            ConnectionState.isConnected.value = false
            Resource.Error("0")
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            Resource.Error(message)
        }
    }

    override suspend fun workers(): Resource<List<HordeWorker>> {
        return try {
            val response = api.getWorkers()
            if (response.isSuccessful) {
                val res = response
                    .body()!!
                    .map(WorkerDto::toHordeWorker)
                Resource.Success(res)
            } else {
                val message = getErrorMessage(response.errorBody())
                Resource.Error(message)
            }
        } catch (e: IOException) {
            ConnectionState.isConnected.value = false
            Resource.Error("0")
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            Resource.Error(message)
        }
    }

    override suspend fun requestGeneration(
        apiKey: String,
        generationInput: GenerationInput
    ): Resource<HordeAsyncRequest> {
        return try {
            val response = api.generationRequest(apiKey, generationInput.toDto())
            when (response.code()) {
                in 200 until 300 -> Resource.Success(
                    response
                        .body()!!
                        .toHordeAsyncRequest()
                )

                400 -> Resource.Error(
                    gerValidationErrorMessage(response.errorBody())
                )

                else -> Resource.Error(
                    getErrorMessage(response.errorBody())
                )
            }
        } catch (e: IOException) {
            ConnectionState.isConnected.value = false
            Resource.Error("0")
        } catch (e: NullPointerException) {
            Resource.Error("Unable to deserialize result of generation request")
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            Resource.Error(message)
        }
    }

    override suspend fun getGenerationRequestStatus(id: String): Resource<HordeRequestStatus> {
        return try {
            val response = api.getGenerationStatus(id)
            if (response.isSuccessful) {
                Resource.Success(
                    response
                        .body()!!
                        .toHordeRequestStatus()
                )
            } else {
                Resource.Error(getErrorMessage(response.errorBody()))
            }
        } catch (e: IOException) {
            ConnectionState.isConnected.value = false
            Resource.Error("0")
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            Resource.Error(message)
        }
    }

    override suspend fun cancelGenerationRequest(id: String): Resource<HordeRequestStatus> {
        return try {
            val response = api.cancelGenerationRequest(id)
            if (response.isSuccessful) {
                Resource.Success(
                    response
                        .body()!!
                        .toHordeRequestStatus()
                )
            } else {
                Resource.Error(getErrorMessage(response.errorBody()))
            }
        } catch (e: IOException) {
            ConnectionState.isConnected.value = false
            Resource.Error("0")
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            Resource.Error(message)
        }
    }

    override fun connectionState(): Flow<Boolean> = ConnectionState.isConnected

    private fun getErrorMessage(responseBody: ResponseBody?): String {
        return try {
            val error = errorConverter.convert(responseBody!!)
            error!!.message
        } catch (e: Exception) {
            "Unknown error"
        }
    }

    private fun gerValidationErrorMessage(responseBody: ResponseBody?): String {
        return try {
            val error = requestValidationErrorConverter.convert(responseBody!!)!!
            var res = error.message
            if (error.errors.additionalProp1.isNotBlank()) res += "\n(${error.errors.additionalProp1})"
            if (error.errors.additionalProp2.isNotBlank()) res += "\n(${error.errors.additionalProp2})"
            if (error.errors.additionalProp3.isNotBlank()) res += "\n(${error.errors.additionalProp3})"
            if (error.errors.additionalProp4.isNotBlank()) res += "\n(${error.errors.additionalProp4})"
            if (error.errors.additionalProp5.isNotBlank()) res += "\n(${error.errors.additionalProp5})"

            res
        } catch (e: Exception) {
            "Unknown validation error"
        }
    }
}