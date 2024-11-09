package ru.kima.intelligentchat.data.util.jsonConverterFactory

import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter

class RequestBodyConverter<T>(
    private val json: Json,
    private val strategy: SerializationStrategy<T>,
    private val contentType: MediaType
) : Converter<T, RequestBody?> {
    override fun convert(p0: T): RequestBody? {
        val string = json.encodeToString(strategy, p0)
        return RequestBody.create(contentType, string)
    }
}
