package ru.kima.intelligentchat.data.koboldcpp

import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.create
import ru.kima.intelligentchat.data.koboldcpp.model.KoboldCppGenerationInputDto
import ru.kima.intelligentchat.data.util.jsonConverterFactory.toConverterFactory
import ru.kima.intelligentchat.domain.koboldcpp.KoboldCppRepository
import ru.kima.intelligentchat.domain.messaging.generation.model.KoboldCppGenerationInput

class KoboldCppRepositoryImpl(
    private val json: Json
) : KoboldCppRepository {
    override suspend fun generate(
        baseUrl: String,
        input: KoboldCppGenerationInput
    ): Result<String> {
        return try {
            val api = Retrofit.Builder()
                .baseUrl(baseUrl.toApiBaseUrl())
                .addConverterFactory(json.toConverterFactory("application/json".toMediaType()))
                .build()
                .create<KoboldCppApi>()

            val response = api.generate(
                KoboldCppGenerationInputDto(
                    prompt = input.prompt,
                    maxContextLength = input.maxContextLength,
                    maxLength = input.maxLength,
                    stopSequence = input.stopSequence,
                    temperature = input.temperature,
                    repetitionPenalty = input.repetitionPenalty,
                    repetitionPenaltyRange = input.repetitionPenaltyRange,
                    repetitionPenaltySlope = input.repetitionPenaltySlope,
                    topP = input.topP,
                    topA = input.topA,
                    topK = input.topK,
                    typical = input.typical,
                    tfs = input.tailFreeSampling,
                    minP = input.minP,
                    mirostat = input.mirostat,
                    mirostatTau = input.mirostatTau,
                    mirostatEta = input.mirostatEta,
                    samplerOrder = input.samplerOrder,
                    grammar = input.grammar
                )
            )
            check(response.isSuccessful) {
                "KoboldCpp returned HTTP ${response.code()}."
            }
            val text = response.body()?.results?.firstOrNull()?.text
                ?: error("KoboldCpp returned no generated text.")
            Result.success(text)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    private fun String.toApiBaseUrl(): String {
        val url = trim().trimEnd('/')
        require(url.startsWith("http://") || url.startsWith("https://")) {
            "KoboldCpp URL must start with http:// or https://."
        }
        return if (url.endsWith("/api/v1")) "$url/" else "$url/api/v1/"
    }
}
