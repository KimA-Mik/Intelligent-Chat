package ru.kima.intelligentchat.presentation.cardDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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
import ru.kima.intelligentchat.presentation.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.cardDetails.events.UiEvent

class CardDetailsViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel(),
    KoinComponent {
    private val cardId = savedStateHandle.getStateFlow("cardId", 0L)
    private val cardTitle = savedStateHandle.getStateFlow("cardTitle", "Title")
    private val cardDescription = savedStateHandle.getStateFlow("cardDescription", "Description")
    private val photoBytes = MutableStateFlow<ByteArray?>(null)

    val state = combine(
        cardId,
        cardTitle,
        cardDescription,
        photoBytes
    ) { cardId, cardTitle, cardDescription, photoBytes ->
        CardDetailsState(
            card = CharacterCard(
                id = cardId,
                name = cardTitle,
                description = cardDescription,
                photoBytes = photoBytes
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CardDetailsState())

    private val getCard: GetCardUseCase by inject()
    private val updateCardAvatar: UpdateCardAvatarUseCase by inject()

    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        savedStateHandle.get<Long>("cardId")?.let {
            loadCard(it)
        }
    }

    fun onEvent(event: CardDetailUserEvent) {
        when (event) {
            CardDetailUserEvent.SelectImageClicked -> onSelectImageClicked()
            is CardDetailUserEvent.UpdateCardImage -> updateCardImage(event.bytes)
        }
    }

    private fun loadCard(id: Long) {
        getCard(id).onEach { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val card = resource.data!!
                    savedStateHandle["cardTitle"] = card.name
                    savedStateHandle["cardDescription"] = card.description
                    photoBytes.value = card.photoBytes
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun onSelectImageClicked() {
        viewModelScope.launch {
            _uiEvents.emit(UiEvent.SelectImage)
        }
    }

    private fun updateCardImage(bytes: ByteArray) {
        if (bytes.isNotEmpty()) {
            updateCardAvatar(state.value.card, bytes).onEach { resource ->
                when (resource) {
                    is Resource.Error -> {
                        _uiEvents.emit(UiEvent.SnackbarMessage(resource.message!!))
                    }

                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        photoBytes.value = resource.data?.photoBytes
                    }
                }

            }.launchIn(viewModelScope)
        }
    }
}