package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.domain.card.model.AltGreeting
import ru.kima.intelligentchat.domain.card.useCase.CreateAlternateGreetingUseCase
import ru.kima.intelligentchat.domain.card.useCase.DeleteAlternateGreetingUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateAlternateGreetingUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardAvatarUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardUseCase
import ru.kima.intelligentchat.domain.tokenizer.useCase.TokenizeTextUseCase
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.UiEvent
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.CardField
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.ImmutableCard
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.toImmutable
import ru.kima.intelligentchat.presentation.navigation.graphs.CARD_ID_ARGUMENT

@OptIn(FlowPreview::class)
class CardDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getCard: GetCardUseCase,
    private val updateCardAvatar: UpdateCardAvatarUseCase,
    private val updateCard: UpdateCardUseCase,
    private val createAlternateGreeting: CreateAlternateGreetingUseCase,
    private val deleteAltGreeting: DeleteAlternateGreetingUseCase,
    private val updateAlternateGreeting: UpdateAlternateGreetingUseCase,
    private val tokenizeText: TokenizeTextUseCase
) : ViewModel() {
    private val cardId = savedStateHandle.getStateFlow(CardField.Id.string, 0L)
    private val card = MutableStateFlow(ImmutableCard())
    private val switchesState = MutableStateFlow(CardDetailsState.SwitchesState())

    private val editableGreeting = savedStateHandle.getStateFlow(EDITABLE_GREETING, 0L)
    private val editableGreetingBuffer =
        savedStateHandle.getStateFlow(EDITABLE_GREETING_BUFFER, String())
    private val additionalSurfaces = combine(
        savedStateHandle.getStateFlow(SHOW_ALT_GREETING, false),
        savedStateHandle.getStateFlow(DELETE_ALT_GREETING_DIALOG, false),
        editableGreeting,
        editableGreetingBuffer
    ) { showAltGreeting, deleteAltGreetingDialog, editableGreeting, editableGreetingBuffer ->
        CardDetailsState.AdditionalSurfaces(
            showAltGreeting = showAltGreeting,
            deleteAltGreetingDialog = deleteAltGreetingDialog,
            editableGreeting = editableGreeting,
            editableGreetingBuffer = editableGreetingBuffer,
        )
    }

    private val tokensCount = MutableStateFlow(CardDetailsState.TokensCount())
    private var cardBeforeTokenization = ImmutableCard()

    val state = combine(
        card,
        additionalSurfaces,
        tokensCount,
        savedStateHandle.getStateFlow(SELECTED_TAB_INDEX, 0),
        switchesState
    ) { card, dialogs, tokensCount, selectedTabIndex, switchesState ->
        CardDetailsState(
            card = card,
            additionalSurfaces = dialogs,
            tokensCount = tokensCount,
            selectedTabIndex = selectedTabIndex,
            switchesState = switchesState
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CardDetailsState())


    private val _uiEvents = MutableStateFlow(Event<UiEvent>(null))
    val uiEvents = _uiEvents.asStateFlow()

    private var greetingToDelete = 0L

    init {
        val id = savedStateHandle.get<Long>(CARD_ID_ARGUMENT)
        viewModelScope.launch {
            if (id == null || id == 0L) {
                _uiEvents.emit(Event(UiEvent.PopBack))
                return@launch
            }

            val newCard = getCard(id).first()
            if (newCard.id == 0L) return@launch

            card.value = newCard.toImmutable()
            tokenizeCard(card.value)

            card
                .debounce(500)
                .onEach { tokenizeCard(it) }
                .collect { updateCard(it.toCard()) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        MainScope().launch(Dispatchers.IO) {
            if (card.value.id == 0L) return@launch

            updateCard(card.value.toCard())
        }
    }

    fun onEvent(event: CardDetailUserEvent) {
        when (event) {
            is CardDetailUserEvent.FieldUpdate -> onFieldUpdate(event.field, event.updatedString)
            is CardDetailUserEvent.FieldSwitch -> onFieldSwitch(event.field)
            CardDetailUserEvent.SelectImageClicked -> onSelectImageClicked()
            is CardDetailUserEvent.UpdateCardImage -> onUpdateCardImage(event.bytes)
            CardDetailUserEvent.OpenAltGreetingsSheet -> onOpenAlternateMessages()
            CardDetailUserEvent.CloseAltGreetingsSheet -> onCloseAlternateMessages()
            CardDetailUserEvent.CreateAltGreeting -> onCreateAltGreeting()
            is CardDetailUserEvent.DeleteAltGreeting -> onDeleteAltGreeting(event.id)
            CardDetailUserEvent.ConfirmDeleteAltGreeting -> onConfirmDeleteAltGreeting()
            CardDetailUserEvent.DismissDeleteAltGreeting -> onDismissDeleteAltGreeting()
            is CardDetailUserEvent.EditAltGreeting -> onEditAltGreeting(event.id)
            CardDetailUserEvent.AcceptAltGreetingEdit -> onAcceptAltGreetingEdit()
            CardDetailUserEvent.RejectAltGreetingEdit -> onRejectAltGreetingEdit()
            is CardDetailUserEvent.UpdateAlternateGreetingBuffer -> onUpdateAlternateGreetingBuffer(
                event.buffer
            )

            is CardDetailUserEvent.SelectTab -> onSelectTab(event.index)
            CardDetailUserEvent.NavigateUp -> onNavigateUp()

        }
    }

    private fun tokenizeCard(card: ImmutableCard) = viewModelScope.launch(Dispatchers.Default) {
        val tokens = tokensCount.value

        val name = if (card.name != cardBeforeTokenization.name)
            tokenizeText(card.name).size else tokens.name
        val description = if (card.description != cardBeforeTokenization.description)
            tokenizeText(card.description).size else tokens.description
        val personality = if (card.personality != cardBeforeTokenization.personality)
            tokenizeText(card.personality).size else tokens.personality
        val scenario = if (card.scenario != cardBeforeTokenization.scenario)
            tokenizeText(card.scenario).size else tokens.scenario
        val firstMes = if (card.firstMes != cardBeforeTokenization.firstMes)
            tokenizeText(card.firstMes).size else tokens.firstMes
        val mesExample = if (card.mesExample != cardBeforeTokenization.mesExample)
            tokenizeText(card.mesExample).size else tokens.mesExample
        val systemPrompt = if (card.systemPrompt != cardBeforeTokenization.systemPrompt)
            tokenizeText(card.systemPrompt).size else tokens.systemPrompt
        val postHistoryInstructions =
            if (card.postHistoryInstructions != cardBeforeTokenization.postHistoryInstructions)
                tokenizeText(card.postHistoryInstructions).size else tokens.postHistoryInstructions

        tokensCount.value = CardDetailsState.TokensCount(
            name = name,
            description = description,
            personality = personality,
            scenario = scenario,
            firstMes = firstMes,
            mesExample = mesExample,
            systemPrompt = systemPrompt,
            postHistoryInstructions = postHistoryInstructions,
        )
        cardBeforeTokenization = card
    }


    private fun onFieldUpdate(field: CardField, update: String) {
        card.update {
            when (field) {
                CardField.Name -> it.copy(name = update)
                CardField.Description -> it.copy(description = update)
                CardField.Personality -> it.copy(personality = update)
                CardField.Scenario -> it.copy(scenario = update)
                CardField.FirstMes -> it.copy(firstMes = update)
                CardField.MesExample -> it.copy(mesExample = update)
                CardField.Creator -> it.copy(creator = update)
                CardField.CharacterVersion -> it.copy(characterVersion = update)
                CardField.CreatorNotes -> it.copy(creatorNotes = update)
                else -> it
            }
        }
    }

    private fun onFieldSwitch(field: CardField) = switchesState.update {
        when (field) {
            CardField.Description -> it.copy(description = !it.description)
            CardField.Personality -> it.copy(personality = !it.personality)
            CardField.Scenario -> it.copy(scenario = !it.scenario)
            CardField.FirstMes -> it.copy(firstMes = !it.firstMes)
            CardField.MesExample -> it.copy(mesExample = !it.mesExample)
            CardField.Creator -> it.copy(creator = !it.creator)
            CardField.CharacterVersion -> it.copy(characterVersion = !it.characterVersion)
            CardField.CreatorNotes -> it.copy(creatorsNote = !it.creatorsNote)
            else -> it
        }
    }


    private fun onSelectImageClicked() {
        viewModelScope.launch {
            _uiEvents.emit(Event(UiEvent.SelectImage))
        }
    }

    private fun onUpdateCardImage(bytes: ByteArray) = viewModelScope.launch {
        if (bytes.isNotEmpty()) {
            updateCardAvatar(state.value.card.id, bytes)?.let { cardName ->
                card.update {
                    it.copy(photoName = cardName)
                }
            }
        }
    }

    private fun onOpenAlternateMessages() {
        savedStateHandle[SHOW_ALT_GREETING] = true
    }

    private fun onCloseAlternateMessages() {
        savedStateHandle[SHOW_ALT_GREETING] = false
    }

    private fun onCreateAltGreeting() = viewModelScope.launch {
        createAlternateGreeting(cardId.value)
        card.value = getCard(card.value.id).first().toImmutable()
    }

    private fun onDeleteAltGreeting(id: Long) = viewModelScope.launch {
        greetingToDelete = id
        savedStateHandle[DELETE_ALT_GREETING_DIALOG] = true
    }

    private fun onConfirmDeleteAltGreeting() = viewModelScope.launch {
        if (greetingToDelete > 0) {
            savedStateHandle[DELETE_ALT_GREETING_DIALOG] = false
            deleteAltGreeting(greetingToDelete)
            greetingToDelete = 0L
            card.value = getCard(card.value.id).first().toImmutable()
        }
    }

    private fun onDismissDeleteAltGreeting() {
        savedStateHandle[DELETE_ALT_GREETING_DIALOG] = false
    }

    private fun onEditAltGreeting(id: Long) {
        val selectedGreeting = state.value.card.alternateGreetings.find { it.id == id } ?: return
        savedStateHandle[EDITABLE_GREETING] = id
        savedStateHandle[EDITABLE_GREETING_BUFFER] = selectedGreeting.body
    }

    private fun onAcceptAltGreetingEdit() = viewModelScope.launch {
        val newBody = editableGreetingBuffer.value
        val selectedGreeting =
            state.value.card
                .alternateGreetings
                .find { it.id == editableGreeting.value }
                ?: return@launch
        val newGreeting = AltGreeting(id = selectedGreeting.id, body = newBody)
        updateAlternateGreeting(newGreeting, state.value.card.id)
        savedStateHandle[EDITABLE_GREETING] = 0L
        savedStateHandle[EDITABLE_GREETING_BUFFER] = String()
        card.value = getCard(card.value.id).first().toImmutable()
    }

    private fun onRejectAltGreetingEdit() {
        savedStateHandle[EDITABLE_GREETING] = 0L
        savedStateHandle[EDITABLE_GREETING_BUFFER] = String()
    }

    private fun onUpdateAlternateGreetingBuffer(buffer: String) {
        savedStateHandle[EDITABLE_GREETING_BUFFER] = buffer
    }

    private fun onSelectTab(index: Int) {
        savedStateHandle[SELECTED_TAB_INDEX] = index
    }

    private fun onNavigateUp() {
        _uiEvents.value = Event(UiEvent.PopBack)
    }

    companion object {
        private const val EDITABLE_GREETING = "editableGreeting"
        private const val EDITABLE_GREETING_BUFFER = "editableGreetingBuffer"
        private const val SHOW_ALT_GREETING = "showAltGreeting"
        private const val DELETE_ALT_GREETING_DIALOG = "deleteAltGreetingDialog"
        private const val SELECTED_TAB_INDEX = "selectedTabIndex"
    }
}