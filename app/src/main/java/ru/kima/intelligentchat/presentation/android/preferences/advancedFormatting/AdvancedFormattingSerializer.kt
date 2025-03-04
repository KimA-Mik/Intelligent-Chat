package ru.kima.intelligentchat.presentation.android.preferences.advancedFormatting

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalSerializationApi::class)
object AdvancedFormattingSerializer : Serializer<AdvancedFormattingSchema> {
    override val defaultValue: AdvancedFormattingSchema
        get() = AdvancedFormattingSchema()

    override suspend fun readFrom(input: InputStream): AdvancedFormattingSchema = try {
        withContext(Dispatchers.IO) {
            return@withContext ProtoBuf.decodeFromByteArray<AdvancedFormattingSchema>(
                input.readBytes()
            )
        }
    } catch (exception: SerializationException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(t: AdvancedFormattingSchema, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(ProtoBuf.encodeToByteArray(t))
        }
    }
}
