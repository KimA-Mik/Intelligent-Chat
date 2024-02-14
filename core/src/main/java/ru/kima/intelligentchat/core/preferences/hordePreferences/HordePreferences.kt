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
    val trustedWorkers: Boolean = false
)

val Context.hordePreferencesDataStore: DataStore<HordePreferences> by dataStore(
    fileName = "horde_preferences.pb",
    serializer = HordePreferencesSerialize
)