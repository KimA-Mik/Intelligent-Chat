package ru.kima.intelligentchat.data.kobold.horde

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import ru.kima.intelligentchat.core.common.ICResult
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.data.kobold.horde.mappers.toActiveModel
import ru.kima.intelligentchat.data.kobold.horde.mappers.toDto
import ru.kima.intelligentchat.data.kobold.horde.mappers.toHordeAsyncRequest
import ru.kima.intelligentchat.data.kobold.horde.mappers.toHordeRequestStatus
import ru.kima.intelligentchat.data.kobold.horde.mappers.toHordeWorker
import ru.kima.intelligentchat.data.kobold.horde.mappers.toUserInfo
import ru.kima.intelligentchat.data.kobold.horde.model.HordeConnectionState
import ru.kima.intelligentchat.data.kobold.horde.model.RequestError
import ru.kima.intelligentchat.data.kobold.horde.model.RequestValidationError
import ru.kima.intelligentchat.data.kobold.horde.model.WorkerDto
import ru.kima.intelligentchat.data.util.jsonConverterFactory.toConverterFactory
import ru.kima.intelligentchat.domain.common.errors.HordeError
import ru.kima.intelligentchat.domain.horde.model.ActiveModel
import ru.kima.intelligentchat.domain.horde.model.GenerationInput
import ru.kima.intelligentchat.domain.horde.model.HordeAsyncRequest
import ru.kima.intelligentchat.domain.horde.model.HordeRequestStatus
import ru.kima.intelligentchat.domain.horde.model.HordeReturnCodes
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
                HordeConnectionState.isConnected.value = true
                Resource.Success(Unit)
            } else {
                Resource.Error("The heart of horde doesn't beat for some reason.")
            }
        } catch (_: IOException) {
            HordeConnectionState.isConnected.value = false
            Resource.Error(HordeRepository.NO_CONNECTION_ERROR)
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
        } catch (_: IOException) {
            HordeConnectionState.isConnected.value = false
            Resource.Error(HordeRepository.NO_CONNECTION_ERROR)
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
        } catch (_: IOException) {
            HordeConnectionState.isConnected.value = false
            Resource.Error(HordeRepository.NO_CONNECTION_ERROR)
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
        } catch (_: IOException) {
            HordeConnectionState.isConnected.value = false
            Resource.Error(HordeRepository.NO_CONNECTION_ERROR)
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            Resource.Error(message)
        }
    }

    override suspend fun requestGeneration(
        apiKey: String,
        generationInput: GenerationInput
    ): ICResult<HordeAsyncRequest, HordeError> {
        return try {
            val response = api.generationRequest(apiKey, generationInput.toDto())
            when (response.code()) {
                in 200 until 300 -> ICResult.Success(
                    response
                        .body()!!
                        .toHordeAsyncRequest()
                )


                else -> ICResult.Error(getHordeError(response.errorBody()))
            }
        } catch (_: IOException) {
            HordeConnectionState.isConnected.value = false
            ICResult.Error(HordeError.NoConnection)
        } catch (_: NullPointerException) {
            ICResult.Error(HordeError.UnknownError("Unable to deserialize result of generation request"))
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            ICResult.Error(HordeError.UnknownError(message))
        }
    }

    override suspend fun getGenerationRequestStatus(id: String): ICResult<HordeRequestStatus, HordeError> {
        return try {
            val response = api.getGenerationStatus(id)
            if (response.isSuccessful) {
                ICResult.Success(
                    response
                        .body()!!
                        .toHordeRequestStatus()
                )
            } else {
                ICResult.Error(getHordeError(response.errorBody()))
            }
        } catch (_: IOException) {
            HordeConnectionState.isConnected.value = false
            ICResult.Error(HordeError.NoConnection)
        } catch (_: NullPointerException) {
            ICResult.Error(HordeError.UnknownError("Unable to deserialize result of check request"))
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            ICResult.Error(HordeError.UnknownError(message))
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
        } catch (_: IOException) {
            HordeConnectionState.isConnected.value = false
            Resource.Error(HordeRepository.NO_CONNECTION_ERROR)
        } catch (e: Exception) {
            val message = e.message ?: e.toString()
            Resource.Error(message)
        }
    }

    override fun connectionState(): Flow<Boolean> = HordeConnectionState.isConnected

    private fun getErrorMessage(responseBody: ResponseBody?): String {
        return try {
            val error = errorConverter.convert(responseBody!!)
            error!!.message
        } catch (_: Exception) {
            "Unknown error"
        }
    }

    private fun getHordeError(responseBody: ResponseBody?): HordeError {
        val error = errorBodyToResponseError(responseBody) ?: return HordeError.UnknownError()
        return when (error.returnCode) {
            HordeReturnCodes.KUDOS_VALIDATION_ERROR -> getValidationError(responseBody, error)
            HordeReturnCodes.IMAGE_VALIDATION_FAILED -> getValidationError(responseBody, error)
            HordeReturnCodes.INVALID_APIKEY -> HordeError.InvalidApiKey
            HordeReturnCodes.TOO_MANY_PROMPTS -> HordeError.TooManyPrompts
            HordeReturnCodes.MAINTENANCE_MODE -> HordeError.MaintenanceMode
            HordeReturnCodes.REQUEST_NOT_FOUND -> HordeError.RequestNotFound
            else -> HordeError.UnknownError(error.message)
        }
    }

    private fun errorBodyToResponseError(responseBody: ResponseBody?): RequestError? {
        return try {
            errorConverter.convert(responseBody!!)
        } catch (_: Exception) {
            null
        }
    }

    private fun getValidationError(responseBody: ResponseBody?, error: RequestError): HordeError {
        return try {
            val prompts =
                gerValidationErrorPrompts(responseBody) ?: return HordeError.UnknownError()
            return HordeError.ValidationError(error.message, prompts)
        } catch (_: Exception) {
            HordeError.UnknownError()
        }
    }

    private fun gerValidationErrorPrompts(responseBody: ResponseBody?): List<String>? {
        return try {
            val res = mutableListOf<String>()
            val error = requestValidationErrorConverter.convert(responseBody!!)!!
            if (error.errors.additionalProp1.isNotBlank()) res.add(error.errors.additionalProp1)
            if (error.errors.additionalProp2.isNotBlank()) res.add(error.errors.additionalProp2)
            if (error.errors.additionalProp3.isNotBlank()) res.add(error.errors.additionalProp3)
            if (error.errors.additionalProp4.isNotBlank()) res.add(error.errors.additionalProp4)
            if (error.errors.additionalProp5.isNotBlank()) res.add(error.errors.additionalProp5)
            res
        } catch (_: Exception) {
            return null
        }
    }
}