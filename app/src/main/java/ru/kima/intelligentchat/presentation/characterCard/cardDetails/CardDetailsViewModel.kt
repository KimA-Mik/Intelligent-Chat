package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
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
import ru.kima.intelligentchat.domain.card.model.AltGreeting
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.CreateAlternateGreetingUseCase
import ru.kima.intelligentchat.domain.card.useCase.DeleteAlternateGreetingUseCase
import ru.kima.intelligentchat.domain.card.useCase.DeleteCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardAvatarUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardUseCase
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.UiEvent

class CardDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getCard: GetCardUseCase,
    private val updateCardAvatar: UpdateCardAvatarUseCase,
    private val updateCard: UpdateCardUseCase,
    private val deleteCard: DeleteCardUseCase,
    private val createAlternateGreeting: CreateAlternateGreetingUseCase,
    private val deleteAltGreeting: DeleteAlternateGreetingUseCase
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
    private val cardAlternateGreetings = MutableStateFlow(emptyList<AltGreeting>())
    private val cardTags = savedStateHandle.getStateFlow(CardField.Tags.string, emptyList<String>())
    private val cardCreator = savedStateHandle.getStateFlow(CardField.Creator.string, "")
    private val cardCharacterVersion =
        savedStateHandle.getStateFlow(CardField.CharacterVersion.string, "")
    private val showAltGreetingSheet = savedStateHandle.getStateFlow("showGreetings", false)

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
        cardCharacterVersion,
        showAltGreetingSheet
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
                alternateGreetings = args[11] as List<AltGreeting>,
                tags = args[12].let { if (it is List<*>) it.filterIsInstance<String>() else emptyList() },
                creator = args[13] as String,
                characterVersion = args[14] as String
            ),
            showAltGreeting = args[15] as Boolean
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CardDetailsState())


    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private var cardJob: Job? = null
    private val isLoaded = savedStateHandle.getStateFlow("isLoaded", false)

    private var greetingToDelete = 0L

    init {
        savedStateHandle.get<Long>(CardField.Id.string)?.let {
            loadCard(it)
        }
    }

    fun onEvent(event: CardDetailUserEvent) {
        when (event) {
            is CardDetailUserEvent.FieldUpdate -> onFieldUpdate(event.field, event.updatedString)
            CardDetailUserEvent.SelectImageClicked -> onSelectImageClicked()
            is CardDetailUserEvent.UpdateCardImage -> onUpdateCardImage(event.bytes)
            CardDetailUserEvent.SaveCard -> onSaveCard()
            CardDetailUserEvent.DeleteCardClicked -> onDeleteCardClicked()
            CardDetailUserEvent.ConfirmDeleteCard -> onDeleteCard()
            CardDetailUserEvent.OpenAltGreetingsSheet -> onOpenAlternateMessages()
            CardDetailUserEvent.CloseAltGreetingsSheet -> onCloseAlternateMessages()
            CardDetailUserEvent.CreateAltGreeting -> onCreateAltGreeting()
            is CardDetailUserEvent.DeleteAltGreeting -> onDeleteAltGreeting(event.id)
            CardDetailUserEvent.ConfirmDeleteAltGreeting -> onConfirmDeleteAltGreeting()
            is CardDetailUserEvent.EditAltGreeting -> onEditAltGreeting(event.id)
            CardDetailUserEvent.AcceptAltGreetingEdit -> onAcceptAltGreetingEdit()
            CardDetailUserEvent.RejectAltGreetingEdit -> onRejectAltGreetingEdit()
        }
    }

    private fun loadCard(id: Long) {
        cardJob = getCard(id).onEach { card ->
            if (!isLoaded.value) {
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
                savedStateHandle[CardField.Tags.string] = card.tags
                savedStateHandle[CardField.Creator.string] = card.creator
                savedStateHandle[CardField.CharacterVersion.string] = card.characterVersion
                savedStateHandle["isLoaded"] = true
            }

            cardAlternateGreetings.value = card.alternateGreetings
        }.launchIn(viewModelScope)
    }

    private fun onSelectImageClicked() {
        viewModelScope.launch {
            _uiEvents.emit(UiEvent.SelectImage)
        }
    }

    private fun onUpdateCardImage(bytes: ByteArray) = viewModelScope.launch {
        if (bytes.isNotEmpty()) {
            updateCardAvatar(state.value.card.id, bytes)
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

    private fun onDeleteCardClicked() {
        viewModelScope.launch {
            _uiEvents.emit(UiEvent.ShowDeleteDialog)
        }
    }

    private fun onDeleteCard() {
        viewModelScope.launch {
            cardJob?.cancel()
            _uiEvents.emit(UiEvent.PopBack)
            deleteCard(state.value.card)
        }
    }

    private fun onOpenAlternateMessages() {
        savedStateHandle["showGreetings"] = true
    }

    private fun onCloseAlternateMessages() {
        savedStateHandle["showGreetings"] = false
    }

    private fun onCreateAltGreeting() = viewModelScope.launch {
        createAlternateGreeting(cardId.value)
    }

    private fun onDeleteAltGreeting(id: Long) = viewModelScope.launch {
        greetingToDelete = id
        _uiEvents.emit(UiEvent.ShowDeleteGreetingDialog)
    }

    private fun onConfirmDeleteAltGreeting() = viewModelScope.launch {
        if (greetingToDelete > 0) {
            deleteAltGreeting(greetingToDelete)
            greetingToDelete = 0
        }
    }

    private fun onEditAltGreeting(id: Long) {
        TODO("Not yet implemented")
    }

    private fun onAcceptAltGreetingEdit() {
        TODO("Not yet implemented")
    }

    private fun onRejectAltGreetingEdit() {
        TODO("Not yet implemented")
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