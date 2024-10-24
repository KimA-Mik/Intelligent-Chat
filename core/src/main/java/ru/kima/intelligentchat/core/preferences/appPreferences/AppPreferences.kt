package ru.kima.intelligentchat.core.preferences.appPreferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable
import ru.kima.intelligentchat.core.common.API_TYPE

@Serializable
data class AppPreferences(
    val selectedPersonaId: Long = 0,
    val selectedApiType: API_TYPE = API_TYPE.HORDE,
    val generationPending: Boolean = false
)

val Context.preferencesDataStore: DataStore<AppPreferences> by dataStore(
    fileName = "preferences.pb",
    serializer = AppPreferencesSerialize
)
