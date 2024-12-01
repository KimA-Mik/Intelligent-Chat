package ru.kima.intelligentchat.presentation.android.preferences.appAppearance

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.kima.intelligentchat.domain.preferences.Preference
import ru.kima.intelligentchat.presentation.android.preferences.ICPreference

class AppAppearanceStore(
    context: Context
) {
    private val dataStore = context.appearanceDataStore
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var _current = MutableStateFlow(AppAppearanceSchema())

    init {
        dataStore.data
            .onEach {
                _current.value = it
            }.launchIn(coroutineScope)
    }

    fun appAppearance() = _current.asStateFlow()

    suspend fun setDarkMode(darkMode: AppAppearance.DarkMode) {
        dataStore.updateData {
            it.copy(
                darkMode = darkMode
            )
        }
    }

    fun darkMode(): Preference<AppAppearance.DarkMode> {
        return ICPreference(
            initialValue = _current.value.darkMode,
            flow = dataStore.data.map { it.darkMode },
            setter = { darkMode ->
                dataStore.updateData {
                    it.copy(
                        darkMode = darkMode
                    )
                }
            }
        )
    }

}