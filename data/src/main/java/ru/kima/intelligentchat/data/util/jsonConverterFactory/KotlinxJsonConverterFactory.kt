package ru.kima.intelligentchat.data.util.jsonConverterFactory

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class KotlinxJsonConverterFactory(
    private val json: Json,
    private val contentType: MediaType
) :
    Converter.Factory() {
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        val strategy = json.serializersModule.serializer(type)
        return RequestBodyConverter(json, strategy, contentType)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val strategy = json.serializersModule.serializer(type)
        return ResponseBodyConverter(json, strategy)
    }
}

fun Json.toConverterFactory(contentType: MediaType): Converter.Factory {
    return KotlinxJsonConverterFactory(this, contentType)
}