package ru.kima.intelligentchat.core.preferences.hordePreferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable

@Serializable
data class HordePreferences(
    val apiToken: String = String(),
    val contextToWorker: Boolean = true,
    val responseToWorker: Boolean = true,
    val trustedWorkers: Boolean = false,
    val userName: String = String(),
    val userId: Int = 0,
    val contextSize: Int = 1024,
    val responseLength: Int = 256
)

val Context.hordePreferencesDataStore: DataStore<HordePreferences> by dataStore(
    fileName = "horde_preferences.pb",
    serializer = HordePreferencesSerialize
)