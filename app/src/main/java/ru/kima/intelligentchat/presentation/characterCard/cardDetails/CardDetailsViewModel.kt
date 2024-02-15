package ru.kima.intelligentchat.presentation.characterCard.cardDetails

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.core.common.Resource
import ru.kima.intelligentchat.domain.card.model.AltGreeting
import ru.kima.intelligentchat.domain.card.useCase.CreateAlternateGreetingUseCase
import ru.kima.intelligentchat.domain.card.useCase.DeleteAlternateGreetingUseCase
import ru.kima.intelligentchat.domain.card.useCase.DeleteCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateAlternateGreetingUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardAvatarUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardUseCase
import ru.kima.intelligentchat.domain.tokenizer.useCase.TokenizeTextUseCase
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.events.UiEvent
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.ImmutableAltGreeting
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.ImmutableCard
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.model.toImmutable

class CardDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getCard: GetCardUseCase,
    private val updateCardAvatar: UpdateCardAvatarUseCase,
    private val updateCard: UpdateCardUseCase,
    private val deleteCard: DeleteCardUseCase,
    private val createAlternateGreeting: CreateAlternateGreetingUseCase,
    private val deleteAltGreeting: DeleteAlternateGreetingUseCase,
    private val updateAlternateGreeting: UpdateAlternateGreetingUseCase,
    private val tokenizeText: TokenizeTextUseCase
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
    private val cardAlternateGreetings = MutableStateFlow(emptyList<ImmutableAltGreeting>())
    private val cardTags = savedStateHandle.getStateFlow(CardField.Tags.string, emptyList<String>())
    private val cardCreator = savedStateHandle.getStateFlow(CardField.Creator.string, "")
    private val cardCharacterVersion =
        savedStateHandle.getStateFlow(CardField.CharacterVersion.string, "")

    private val deleteCardDialog = MutableStateFlow(false)
    private val showAltGreetingSheet = savedStateHandle.getStateFlow(SHOW_GREETINGS, false)
    private val deleteAltGreetingDialog = MutableStateFlow(false)

    private val editableGreeting = savedStateHandle.getStateFlow(EDITABLE_GREETING, 0L)
    private val editableGreetingBuffer =
        savedStateHandle.getStateFlow(EDITABLE_GREETING_BUFFER, String())

    private val nameTokensCount = MutableStateFlow(0)
    private val descriptionTokensCount = MutableStateFlow(0)
    private val personalityTokensCount = MutableStateFlow(0)
    private val scenarioTokensCount = MutableStateFlow(0)
    private val firstMesTokensCount = MutableStateFlow(0)
    private val mesExampleTokensCount = MutableStateFlow(0)
    private val systemPromptTokensCount = MutableStateFlow(0)
    private val postHistoryInstructionsTokensCount = MutableStateFlow(0)

    val state = combine(
        cardId, photoBytes, cardName,
        cardDescription, cardPersonality, cardScenario,
        cardFirstMes, cardMesExample, cardCreatorNotes,
        cardSystemPrompt, cardPostHistoryInstructions, cardAlternateGreetings,
        cardTags, cardCreator, cardCharacterVersion, deleteCardDialog, showAltGreetingSheet,
        deleteAltGreetingDialog, editableGreeting, editableGreetingBuffer,
        nameTokensCount, descriptionTokensCount, personalityTokensCount,
        scenarioTokensCount, firstMesTokensCount, mesExampleTokensCount,
        systemPromptTokensCount, postHistoryInstructionsTokensCount
    ) { args ->

        @Suppress("UNCHECKED_CAST")
        CardDetailsState(
            card = ImmutableCard(
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
                alternateGreetings = args[11] as List<ImmutableAltGreeting>,
                tags = args[12].let { if (it is List<*>) it.filterIsInstance<String>() else emptyList() },
                creator = args[13] as String,
                characterVersion = args[14] as String
            ),
            deleteCardDialog = args[15] as Boolean,
            showAltGreeting = args[16] as Boolean,
            deleteAltGreetingDialog = args[17] as Boolean,
            editableGreeting = args[18] as Long,
            editableGreetingBuffer = args[19] as String,
            nameTokensCount = args[20] as Int,
            descriptionTokensCount = args[21] as Int,
            personalityTokensCount = args[22] as Int,
            scenarioTokensCount = args[23] as Int,
            firstMesTokensCount = args[24] as Int,
            mesExampleTokensCount = args[25] as Int,
            systemPromptTokensCount = args[26] as Int,
            postHistoryInstructionsTokensCount = args[27] as Int,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CardDetailsState())


    private val _uiEvents = MutableSharedFlow<UiEvent>()
    val uiEvents = _uiEvents.asSharedFlow()

    private var cardJob: Job? = null
    private val isLoaded = savedStateHandle.getStateFlow(IS_LOADED, false)

    private var greetingToDelete = 0L

    init {
        savedStateHandle.get<Long>(CardField.Id.string)?.let {
            loadCard(it)
        }
        tokenizeCardOnLoad()
    }

    fun onEvent(event: CardDetailUserEvent) {
        when (event) {
            is CardDetailUserEvent.FieldUpdate -> onFieldUpdate(event.field, event.updatedString)
            CardDetailUserEvent.SelectImageClicked -> onSelectImageClicked()
            is CardDetailUserEvent.UpdateCardImage -> onUpdateCardImage(event.bytes)
            CardDetailUserEvent.SaveCard -> onSaveCard()
            CardDetailUserEvent.DeleteCardClicked -> onDeleteCardClicked()
            CardDetailUserEvent.ConfirmDeleteCard -> onDeleteCard()
            CardDetailUserEvent.DismissDeleteCard -> onDismissDeleteCard()
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
                savedStateHandle[IS_LOADED] = true
            }

            cardAlternateGreetings.value = card.alternateGreetings.map { it.toImmutable() }
        }.launchIn(viewModelScope)
    }

    private fun tokenizeCardOnLoad() = viewModelScope.launch(Dispatchers.Default) {
        //resume coroutine when it card loaded
        isLoaded
            .filter { it }
            .first()

        nameTokensCount.value = tokenizeText(cardName.value).size
        descriptionTokensCount.value = tokenizeText(cardDescription.value).size
        personalityTokensCount.value = tokenizeText(cardPersonality.value).size
        scenarioTokensCount.value = tokenizeText(cardScenario.value).size
        firstMesTokensCount.value = tokenizeText(cardFirstMes.value).size
        mesExampleTokensCount.value = tokenizeText(cardMesExample.value).size
        systemPromptTokensCount.value = tokenizeText(cardSystemPrompt.value).size
        postHistoryInstructionsTokensCount.value =
            tokenizeText(cardPostHistoryInstructions.value).size
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
        if (tokenizableFields.contains(field)) {
            viewModelScope.launch(Dispatchers.Default) {
                val tokensCount = tokenizeText(update).size
                tokenizableFields[field]!!.value = tokensCount
            }
        }
    }

    private fun onSaveCard() {
        viewModelScope.launch {
            updateCard(state.value.card.toCard()).onEach { resource ->
                when (resource) {
                    is Resource.Error -> _uiEvents.emit(UiEvent.SnackbarMessage(resource.message!!))
                    is Resource.Loading -> {}
                    is Resource.Success -> _uiEvents.emit(UiEvent.SnackbarMessage("The card has been saved"))
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun onDeleteCardClicked() {
        deleteCardDialog.value = true
    }

    private fun onDeleteCard() {
        deleteCardDialog.value = false
        viewModelScope.launch {
            cardJob?.cancel()
            _uiEvents.emit(UiEvent.PopBack)
            deleteCard(state.value.card.toCard())
        }
    }

    private fun onDismissDeleteCard() {
        deleteCardDialog.value = false
    }

    private fun onOpenAlternateMessages() {
        savedStateHandle[SHOW_GREETINGS] = true
    }

    private fun onCloseAlternateMessages() {
        savedStateHandle[SHOW_GREETINGS] = false
    }

    private fun onCreateAltGreeting() = viewModelScope.launch {
        createAlternateGreeting(cardId.value)
    }

    private fun onDeleteAltGreeting(id: Long) = viewModelScope.launch {
        greetingToDelete = id
        deleteAltGreetingDialog.value = true
    }

    private fun onConfirmDeleteAltGreeting() = viewModelScope.launch {
        if (greetingToDelete > 0) {
            deleteAltGreetingDialog.value = false
            deleteAltGreeting(greetingToDelete)
            greetingToDelete = 0L
        }
    }

    private fun onDismissDeleteAltGreeting() {
        deleteAltGreetingDialog.value = false
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
    }

    private fun onRejectAltGreetingEdit() {
        savedStateHandle[EDITABLE_GREETING] = 0L
        savedStateHandle[EDITABLE_GREETING_BUFFER] = String()
    }

    private fun onUpdateAlternateGreetingBuffer(buffer: String) {
        savedStateHandle[EDITABLE_GREETING_BUFFER] = buffer
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

    private val tokenizableFields = mapOf(
        CardField.Name to nameTokensCount,
        CardField.Description to descriptionTokensCount,
        CardField.Personality to personalityTokensCount,
        CardField.Scenario to scenarioTokensCount,
        CardField.FirstMes to firstMesTokensCount,
        CardField.MesExample to mesExampleTokensCount,
        CardField.SystemPrompt to systemPromptTokensCount,
        CardField.PostHistoryInstructions to postHistoryInstructionsTokensCount
    )

    companion object {
        private const val IS_LOADED = "isLoaded"
        private const val SHOW_GREETINGS = "showGreetings"
        private const val EDITABLE_GREETING = "editableGreeting"
        private const val EDITABLE_GREETING_BUFFER = "editableGreetingBuffer"
    }
}