package ru.kima.intelligentchat.presentation.settings.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.presentation.settings.root.events.SettingsRootAction
import ru.kima.intelligentchat.presentation.settings.root.events.SettingsRootUiEvent
import ru.kima.intelligentchat.presentation.settings.screen.AdvancedFormattingScreen
import ru.kima.intelligentchat.presentation.settings.screen.AppearanceSettingsScreen
import ru.kima.intelligentchat.presentation.settings.screen.ChatSettingsScreen
import ru.kima.intelligentchat.presentation.settings.screen.SettingsScreen

class SettingsRootViewModel : ViewModel() {

    private val settings = MutableStateFlow<ImmutableList<SettingsScreen>>(
        persistentListOf(
            AppearanceSettingsScreen,
            ChatSettingsScreen,
            AdvancedFormattingScreen
        )
    )
    private val selectedSetting = MutableStateFlow<SettingsScreen?>(null)
    val state = combine(
        settings, selectedSetting
    ) { settings, selectedSetting ->
        SettingsRootState(
            screens = settings,
            selectedScreen = selectedSetting
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), SettingsRootState())

    private val _uiEvent = MutableStateFlow<Event<SettingsRootUiEvent>>(Event(null))
    val uiEvent = _uiEvent.asStateFlow()

    fun onEvent(action: SettingsRootAction) {
        when (action) {
            SettingsRootAction.ClearScreen -> onClearScreen()
            is SettingsRootAction.SelectScreen -> onSelectScreen(action.screen)
            SettingsRootAction.BackPressed -> onBackPressed()
        }
    }

    private fun onClearScreen() {
        selectedSetting.value = null
    }

    private fun onSelectScreen(screen: SettingsScreen) {
        selectedSetting.value = screen
    }

    private fun onBackPressed() {
        if (selectedSetting.value != null) {
            selectedSetting.value = null
        } else {
            _uiEvent.value = Event(SettingsRootUiEvent.PopBack)
        }
    }
}