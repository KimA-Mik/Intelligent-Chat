package ru.kima.intelligentchat.data.util.jsonConverterFactory

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.json.Json
import okhttp3.ResponseBody
import retrofit2.Converter

class ResponseBodyConverter<T>(
    private val json: Json,
    private val strategy: DeserializationStrategy<T>
) : Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T? {
        val string = value.string()
        return json.decodeFromString(strategy, string)
    }
}
