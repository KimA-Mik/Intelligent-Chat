package ru.kima.intelligentchat.presentation.android.preferences.textCompletion

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
object TextCompletionPreferencesSerializer : Serializer<TextCompletionPreferencesSchema> {
    override val defaultValue = TextCompletionPreferencesSchema()

    override suspend fun readFrom(input: InputStream): TextCompletionPreferencesSchema = try {
        withContext(Dispatchers.IO) {
            ProtoBuf.decodeFromByteArray(input.readBytes())
        }
    } catch (exception: SerializationException) {
        throw CorruptionException("Cannot read Text Completion preferences.", exception)
    }

    override suspend fun writeTo(t: TextCompletionPreferencesSchema, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(ProtoBuf.encodeToByteArray(t))
        }
    }
}
