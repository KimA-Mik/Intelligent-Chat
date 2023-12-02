package ru.kima.intelligentchat.presentation.cardDetails

import android.graphics.Bitmap
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
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardAvatarUseCase
import ru.kima.intelligentchat.presentation.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.cardDetails.events.UiEvent

class CardDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getCard: GetCardUseCase,
    private val updateCardAvatar: UpdateCardAvatarUseCase
) : ViewModel() {
    private val cardId = savedStateHandle.getStateFlow("cardId", 0L)
    private val photoBytes = MutableStateFlow<Bitmap?>(null)
    private val cardName = savedStateHandle.getStateFlow("cardName", "")
    private val cardDescription = savedStateHandle.getStateFlow("cardDescription", "")
    private val cardPersonality = savedStateHandle.getStateFlow("cardPersonality", "")
    private val cardScenario = savedStateHandle.getStateFlow("cardScenario", "")
    private val cardFirstMes = savedStateHandle.getStateFlow("cardFirstMes", "")
    private val cardMesExample = savedStateHandle.getStateFlow("cardMesExample", "")
    private val cardCreatorNotes = savedStateHandle.getStateFlow("cardCreatorNotes", "")
    private val cardSystemPrompt = savedStateHandle.getStateFlow("cardSystemPrompt", "")
    private val cardPostHistoryInstructions =
        savedStateHandle.getStateFlow("cardPostHistoryInstructions", "")
    private val cardAlternateGreetings =
        savedStateHandle.getStateFlow("cardAlternateGreetings", emptyList<String>())
    private val cardTags = savedStateHandle.getStateFlow("cardTags", emptyList<String>())
    private val cardCreator = savedStateHandle.getStateFlow("cardCreator", "")
    private val cardCharacterVersion = savedStateHandle.getStateFlow("cardCharacterVersion", "")

    val state = combine(
        cardId,
        photoBytes,
        cardName,
        cardDescription,
        cardPersonality,
        cardScenario,
        cardFirstMes,
        cardMesExample,
        cardCreatorNotes,
        cardSystemPrompt,
        cardPostHistoryInstructions,
        cardAlternateGreetings,
        cardTags,
        cardCreator,
        cardCharacterVersion
    ) { args ->

        @Suppress("UNCHECKED_CAST")
        CardDetailsState(
            card = CharacterCard(
                id = args[0] as Long,
                photoBytes = args[1]?.let { it as Bitmap },
                name = args[2] as String,
                description = args[3] as String,
                personality = args[4] as String,
                scenario = args[5] as String,
                firstMes = args[6] as String,
                mesExample = args[7] as String,
                creatorNotes = args[8] as String,
                systemPrompt = args[9] as String,
                postHistoryInstructions = args[10] as String,
                alternateGreetings = args[11] as List<String>,
                tags = args[12].let { if (it is List<*>) it.filterIsInstance<String>() else emptyList() },
                creator = args[13] as String,
                characterVersion = args[14] as String
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CardDetailsState())


    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    init {
        val isLoaded = savedStateHandle.get<Boolean>("isLoaded")

        if (isLoaded == null || !isLoaded) {
            savedStateHandle.get<Long>("cardId")?.let {
                loadCard(it)
                savedStateHandle["isLoaded"] = true
            }
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

                    savedStateHandle["cardName"] = card.name
                    photoBytes.value = card.photoBytes
                    savedStateHandle["cardDescription"] = card.description
                    savedStateHandle["cardPersonality"] = card.personality
                    savedStateHandle["cardScenario"] = card.scenario
                    savedStateHandle["cardFirstMes"] = card.firstMes
                    savedStateHandle["cardMesExample"] = card.mesExample
                    savedStateHandle["cardCreatorNotes"] = card.creatorNotes
                    savedStateHandle["cardSystemPrompt"] = card.systemPrompt
                    savedStateHandle["cardPostHistoryInstructions"] = card.postHistoryInstructions
                    savedStateHandle["cardAlternateGreetings"] = card.alternateGreetings
                    savedStateHandle["cardTags"] = card.tags
                    savedStateHandle["cardCreator"] = card.creator
                    savedStateHandle["cardCharacterVersion"] = card.characterVersion
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