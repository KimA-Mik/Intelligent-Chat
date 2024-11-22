package ru.kima.intelligentchat.presentation.showImage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase

class ShowImageViewModel(
    savedStateHandle: SavedStateHandle,
    private val getCard: GetCardUseCase
) : ViewModel() {
    private val _photoName = MutableStateFlow<String?>(null)
    val photoName = _photoName.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        val cardId = savedStateHandle.get<Long>("cardId")
        if (cardId != null && cardId > 0) {
            loadImageFromCard(cardId)
        }
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            UserEvent.OnCloseClicked -> onCloseClicked()
        }
    }

    private fun loadImageFromCard(cardId: Long) {
        getCard(cardId).onEach { card ->
            _photoName.value = card.photoName
        }.launchIn(viewModelScope)
    }

    private fun onLoadError(message: String) = viewModelScope.launch {
        _uiEvents.emit(UiEvent.ShowSnackbar(message))
        _uiEvents.emit(UiEvent.Close)
    }

    private fun onCloseClicked() = viewModelScope.launch {
        _uiEvents.emit(UiEvent.Close)
    }

    sealed interface UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent
        data object Close : UiEvent
    }

    sealed interface UserEvent {
        data object OnCloseClicked : UserEvent
    }
}