package ru.kima.intelligentchat.core.preferences.hordePreferences


import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalSerializationApi::class)
object HordePreferencesSerialize : Serializer<HordePreferences> {
    override val defaultValue: HordePreferences
        get() = HordePreferences()

    override suspend fun readFrom(input: InputStream): HordePreferences {
        try {
            return ProtoBuf.decodeFromByteArray<HordePreferences>(input.readBytes())
        } catch (exception: SerializationException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: HordePreferences, output: OutputStream) {
        output.write(ProtoBuf.encodeToByteArray(t))
    }
}
