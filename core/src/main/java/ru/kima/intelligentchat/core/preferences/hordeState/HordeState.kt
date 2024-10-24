package ru.kima.intelligentchat.core.preferences.hordeState

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.core.preferences.hordeState.model.HordeModelInfo

@Serializable
data class HordeState(
    val apiToken: String = String(),
    val contextToWorker: Boolean = true,
    val responseToWorker: Boolean = true,
    val trustedWorkers: Boolean = false,
    val userName: String = String(),
    val userId: Int = 0,
    val userContextSize: Int = 1024,
    val actualContextSize: Int = 0,
    val userResponseLength: Int = 256,
    val actualResponseLength: Int = 0,
    val modelsInfo: Map<String, HordeModelInfo> = emptyMap(),
    val selectedModels: List<String> = emptyList(),
    val selectedPreset: Long = 1L,
    val generationId: String? = null,
)

val Context.hordeStateDataStore: DataStore<HordeState> by dataStore(
    fileName = "horde_preferences.pb",
    serializer = HordeStateSerialize
)