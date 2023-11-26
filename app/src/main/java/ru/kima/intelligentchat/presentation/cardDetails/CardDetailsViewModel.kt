package ru.kima.intelligentchat.presentation.cardDetails

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kima.intelligentchat.common.Resource
import ru.kima.intelligentchat.domain.model.CharacterCard
import ru.kima.intelligentchat.domain.useCase.characterCard.GetCardUseCase
import ru.kima.intelligentchat.domain.useCase.characterCard.UpdateCardAvatarUseCase

class CardDetailsViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel(),
    KoinComponent {
    private val cardTitle = savedStateHandle.getStateFlow("cardTitle", "Title")
    private val cardDescription = savedStateHandle.getStateFlow("cardDescription", "Description")
    private val photoFilePath = savedStateHandle.getStateFlow<String?>("photoFilePath", null)

    val state = combine(
        cardTitle,
        cardDescription,
        photoFilePath
    ) { cardTitle, cardDescription, photoFilePath ->
        CardDetailsState(
            card = CharacterCard(
                name = cardTitle,
                description = cardDescription,
                photoFilePath = photoFilePath
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CardDetailsState())

    private val getCard: GetCardUseCase by inject()
    private val updateCardAvatar: UpdateCardAvatarUseCase by inject()

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        savedStateHandle.get<Int>("cardId")?.let {
            loadCard(it)
        }
    }

    private fun loadCard(id: Int) {
        getCard(id).onEach { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val card = resource.data!!
                    savedStateHandle["cardTitle"] = card.name
                    savedStateHandle["cardDescription"] = card.description
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onSelectImageClicked() {
        viewModelScope.launch {
            _uiEvents.emit(UiEvent.SelectImage)
        }
    }

    fun loadCardAvatar(uri: Uri?) {
        uri?.let {
            updateCardAvatar(state.value.card, it).onEach { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _uiEvents.emit(UiEvent.SnackbarMessage(resource.message!!))
                    }

                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        savedStateHandle["photoFilePath"] = resource.data?.photoFilePath
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}