package ru.kima.intelligentchat.presentation.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityViewModel : ViewModel() {
    private val _selectedNavigationDrawerItem = MutableStateFlow(0)
    val selectedNavigationDrawerItem = _selectedNavigationDrawerItem.asStateFlow()

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.SelectNavigationDrawerItem -> onSelectNavigationDrawerItem(event.index)
        }
    }

    private fun onSelectNavigationDrawerItem(index: Int) {
        _selectedNavigationDrawerItem.value = index
    }

    sealed interface UserEvent {
        data class SelectNavigationDrawerItem(val index: Int) : UserEvent
    }
}