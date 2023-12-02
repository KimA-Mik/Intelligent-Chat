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
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardUseCase
import ru.kima.intelligentchat.presentation.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.cardDetails.events.UiEvent

class CardDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getCard: GetCardUseCase,
    private val updateCardAvatar: UpdateCardAvatarUseCase,
    private val updateCard: UpdateCardUseCase
) : ViewModel() {
    private val cardId = savedStateHandle.getStateFlow(CardField.Id.string, 0L)
    private val photoBytes = MutableStateFlow<Bitmap?>(null)
    private val cardName = savedStateHandle.getStateFlow(CardField.Name.string, "")
    private val cardDescription = savedStateHandle.getStateFlow(CardField.Description.string, "")
    private val cardPersonality = savedStateHandle.getStateFlow(CardField.Personality.string, "")
    private val cardScenario = savedStateHandle.getStateFlow(CardField.Scenario.string, "")
    private val cardFirstMes = savedStateHandle.getStateFlow(CardField.FirstMes.string, "")
    private val cardMesExample = savedStateHandle.getStateFlow(CardField.MesExample.string, "")
    private val cardCreatorNotes = savedStateHandle.getStateFlow(CardField.CreatorNotes.string, "")
    private val cardSystemPrompt = savedStateHandle.getStateFlow(CardField.SystemPrompt.string, "")
    private val cardPostHistoryInstructions =
        savedStateHandle.getStateFlow(CardField.PostHistoryInstructions.string, "")
    private val cardAlternateGreetings =
        savedStateHandle.getStateFlow(CardField.AlternateGreetings.string, emptyList<String>())
    private val cardTags = savedStateHandle.getStateFlow(CardField.Tags.string, emptyList<String>())
    private val cardCreator = savedStateHandle.getStateFlow(CardField.Creator.string, "")
    private val cardCharacterVersion =
        savedStateHandle.getStateFlow(CardField.CharacterVersion.string, "")

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
            savedStateHandle.get<Long>(CardField.Id.string)?.let {
                loadCard(it)
                savedStateHandle["isLoaded"] = true
            }
        }
    }

    fun onEvent(event: CardDetailUserEvent) {
        when (event) {
            is CardDetailUserEvent.FieldUpdate -> onFieldUpdate(event.field, event.updatedString)
            CardDetailUserEvent.SelectImageClicked -> onSelectImageClicked()
            is CardDetailUserEvent.UpdateCardImage -> onUpdateCardImage(event.bytes)
            CardDetailUserEvent.SaveCard -> onSaveCard()
        }
    }

    private fun loadCard(id: Long) {
        getCard(id).onEach { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val card = resource.data!!

                    savedStateHandle[CardField.Name.string] = card.name
                    photoBytes.value = card.photoBytes
                    savedStateHandle[CardField.Description.string] = card.description
                    savedStateHandle[CardField.Personality.string] = card.personality
                    savedStateHandle[CardField.Scenario.string] = card.scenario
                    savedStateHandle[CardField.FirstMes.string] = card.firstMes
                    savedStateHandle[CardField.MesExample.string] = card.mesExample
                    savedStateHandle[CardField.CreatorNotes.string] = card.creatorNotes
                    savedStateHandle[CardField.SystemPrompt.string] = card.systemPrompt
                    savedStateHandle[CardField.PostHistoryInstructions.string] =
                        card.postHistoryInstructions
                    savedStateHandle[CardField.AlternateGreetings.string] = card.alternateGreetings
                    savedStateHandle[CardField.Tags.string] = card.tags
                    savedStateHandle[CardField.Creator.string] = card.creator
                    savedStateHandle[CardField.CharacterVersion.string] = card.characterVersion
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun onSelectImageClicked() {
        viewModelScope.launch {
            _uiEvents.emit(UiEvent.SelectImage)
        }
    }

    private fun onUpdateCardImage(bytes: ByteArray) {
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

    private fun onFieldUpdate(field: CardField, update: String) {
        savedStateHandle[field.string] = update
    }

    private fun onSaveCard() {
        viewModelScope.launch {
            updateCard(state.value.card).onEach { resource ->
                when (resource) {
                    is Resource.Error -> _uiEvents.emit(UiEvent.SnackbarMessage(resource.message!!))
                    is Resource.Loading -> {}
                    is Resource.Success -> _uiEvents.emit(UiEvent.SnackbarMessage("The card has been saved"))
                }
            }.launchIn(viewModelScope)
        }
    }

    enum class CardField(val string: String) {
        Id("cardId"),
        Name("cardName"),
        Description("cardDescription"),
        Personality("cardPersonality"),
        Scenario("cardScenario"),
        FirstMes("cardFirstMes"),
        MesExample("cardMesExample"),
        CreatorNotes("cardCreatorNotes"),
        SystemPrompt("cardSystemPrompt"),
        PostHistoryInstructions("cardPostHistoryInstructions"),
        AlternateGreetings("cardAlternateGreetings"),
        Tags("cardTags"),
        Creator("cardCreator"),
        CharacterVersion("cardCharacterVersion")
    }
}