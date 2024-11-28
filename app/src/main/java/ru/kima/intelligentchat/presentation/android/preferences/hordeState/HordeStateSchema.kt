package ru.kima.intelligentchat.presentation.android.preferences.hordeState

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.presentation.android.preferences.hordeState.model.HordeModelInfoSchema

@Serializable
data class HordeStateSchema(
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
    val modelsInfo: Map<String, HordeModelInfoSchema> = emptyMap(),
    val selectedModels: List<String> = emptyList(),
    val selectedPreset: Long = 1L,
    val generationId: String? = null,
)

val Context.hordeStateDataStore: DataStore<HordeStateSchema> by dataStore(
    fileName = "horde_preferences.pb",
    serializer = HordeStateSerializer
)