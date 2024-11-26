package ru.kima.intelligentchat.presentation.android.preferences.hordeState


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
object HordeStateSerializer : Serializer<HordeStateSchema> {
    override val defaultValue: HordeStateSchema
        get() = HordeStateSchema()

    override suspend fun readFrom(input: InputStream): HordeStateSchema {
        try {
            return ProtoBuf.decodeFromByteArray<HordeStateSchema>(input.readBytes())
        } catch (exception: SerializationException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: HordeStateSchema, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(ProtoBuf.encodeToByteArray(t))
        }
    }
}
