package ru.kima.intelligentchat.core.preferences

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream


@OptIn(ExperimentalSerializationApi::class)
object AppPreferencesSerialize : Serializer<AppPreferences> {
    override val defaultValue: AppPreferences
        get() = AppPreferences()

    override suspend fun readFrom(input: InputStream): AppPreferences {
        try {
            return ProtoBuf.decodeFromByteArray<AppPreferences>(input.readBytes())
        } catch (exception: SerializationException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AppPreferences, output: OutputStream) {
        output.write(ProtoBuf.encodeToByteArray(t))
    }
}

val Context.preferencesDataStore: DataStore<AppPreferences> by dataStore(
    fileName = "preferences.pb",
    serializer = AppPreferencesSerialize
)