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
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.image.useCase.GetImageUseCase

class ShowImageViewModel(
    savedStateHandle: SavedStateHandle,
    getImage: GetImageUseCase
) : ViewModel() {
    private val _imageByteArray = MutableStateFlow(byteArrayOf())
    val imageByteArray = _imageByteArray.asStateFlow()

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        val imageName = savedStateHandle.get<String>("imageName")
        if (imageName.isNullOrBlank()) {
            onLoadError("There are no image")
        } else {
            getImage(imageName).onEach { resource ->
                when (resource) {
                    is Resource.Error -> onLoadError(resource.message!!)
                    is Resource.Loading -> {}
                    is Resource.Success -> _imageByteArray.value = resource.data!!
                }
            }.launchIn(viewModelScope)
        }
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            UserEvent.onCloseClicked -> onCloseClicked()
        }
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
        data object onCloseClicked : UserEvent
    }
}