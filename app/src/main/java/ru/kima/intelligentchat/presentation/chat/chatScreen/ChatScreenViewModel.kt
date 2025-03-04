package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.model.SwipeDirection
import ru.kima.intelligentchat.domain.chat.useCase.CreateAndSelectChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToFullChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.BranchChatFromMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.DeleteCurrentSwipeUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.DeleteMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.EditMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.MoveMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.RestoreMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.RestoreSwipeUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.SwipeFirstMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.SwipeMessageUseCase
import ru.kima.intelligentchat.domain.common.valueOr
import ru.kima.intelligentchat.domain.messaging.model.MessagingIndicator
import ru.kima.intelligentchat.domain.messaging.useCase.CancelMessageUseCase
import ru.kima.intelligentchat.domain.messaging.useCase.SendMessageUseCase
import ru.kima.intelligentchat.domain.messaging.useCase.SubscribeToMessagingStatus
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.useCase.GetPersonasUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.domain.preferences.chatSettings.ChatSettingsRepository
import ru.kima.intelligentchat.domain.utils.combine
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UiEvent
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent
import ru.kima.intelligentchat.presentation.chat.chatScreen.mappers.toDisplayCard
import ru.kima.intelligentchat.presentation.chat.chatScreen.mappers.toDisplayChat
import ru.kima.intelligentchat.presentation.chat.chatScreen.mappers.toImmutable
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayCard
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ImmutableChatAppearance
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ImmutableMessagingIndicator
import ru.kima.intelligentchat.presentation.navigation.graphs.CARD_ID_ARGUMENT

class ChatScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val preferences: GetPreferencesUseCase,
    private val editMessage: EditMessageUseCase,
    private val getPersonas: GetPersonasUseCase,
    private val sendMessage: SendMessageUseCase,
    private val moveMessage: MoveMessageUseCase,
    private val getCharacterCard: GetCardUseCase,
    private val swipeMessage: SwipeMessageUseCase,
    private val restoreSwipe: RestoreSwipeUseCase,
    private val cancelMessage: CancelMessageUseCase,
    private val deleteMessage: DeleteMessageUseCase,
    private val restoreMessage: RestoreMessageUseCase,
    private val messagingStatus: SubscribeToMessagingStatus,
    private val swipeFirstMessage: SwipeFirstMessageUseCase,
    private val deleteCurrentSwipe: DeleteCurrentSwipeUseCase,
    private val subscribeToFullChat: SubscribeToFullChatUseCase,
    private val createAndSelectChat: CreateAndSelectChatUseCase,
    private val branchChatFromMessage: BranchChatFromMessageUseCase,
    private val chatSettingsRepository: ChatSettingsRepository,
) : ViewModel() {
    private val characterCard = MutableStateFlow(CharacterCard.default())
    private val displayCard = MutableStateFlow(DisplayCard())

    private val _state = MutableStateFlow(ChatState())
    val state = _state.asStateFlow()

    private val _uiEvent = MutableStateFlow(Event<UiEvent>(null))
    val uiEvent = _uiEvent.asStateFlow()

    private var chatJob: Job? = null
    private val _fullChat = MutableStateFlow(FullChat())

    init {
        initialize()
    }

    private fun initialize() {
        val id = savedStateHandle.get<Long>(CARD_ID_ARGUMENT)
        if (id == null || id == 0L) {
            return
        }

        loadCard(id)

        viewModelScope.launch(Dispatchers.IO) {
            characterCard.collect { card ->
                if (card.id < 1L) return@collect
                chatJob?.cancel()
                chatJob = viewModelScope.launch {
                    subscribeToFullChat(card.selectedChat).collect {
                        _fullChat.value = it.valueOr { error ->
                            when (error) {
                                SubscribeToFullChatUseCase.Error.UnknownError -> FullChat()
                                SubscribeToFullChatUseCase.Error.ChatNotFound -> {
                                    createAndSelectChat(id)
                                    FullChat()
                                }
                            }
                        }
                    }
                }
            }
        }

        val personas = getPersonas()
        val personasNames = loadPersonasNames(personas)
        val personasImages = loadPersonasImages(personas)

        val chatInfo = combine(
            characterCard, displayCard, _fullChat, personasNames, personasImages, preferences(),
        ) { characterCard, displayCard, fullChat, names, images, preferences ->
            ChatState.ChatInfo(
                characterCard = displayCard,
                fullChat = fullChat.toDisplayChat(
                    card = characterCard,
                    selectedPersona = preferences.selectedPersonaId,
                    personasNames = names,
                    personasImages = images
                )
            )
        }

        val chat = combine(
            chatSettingsRepository.showDate().subscribe(),
            chatSettingsRepository.showNumber().subscribe()
        ) { showDate, showNumber ->
            ImmutableChatAppearance(
                showDate = showDate,
                showNumber = showNumber
            )
        }

        val stateFlow = combine(
            chatInfo,
            messagingStatus()
                .onEach { if (it is MessagingIndicator.Done) _uiEvent.emit(Event(UiEvent.ScrollDown)) },
            savedStateHandle.getStateFlow(MESSAGE_INPUT_BUFFER, ""),
            savedStateHandle.getStateFlow(MESSAGE_EDIT_BUFFER, ""),
            savedStateHandle.getStateFlow(EDITED_MESSAGE_ID, EMPTY_EDITED_MESSAGE_ID),
            savedStateHandle.getStateFlow(OPEN_URI_REQUEST, false),
            savedStateHandle.getStateFlow(URI_TO_OPEN, ""),
            chat,
        ) { info, messagingStatus, inputMessageBuffer, editMessageBuffer, editMessageId, openUriRequest, uriToOpen, chatAppearance ->
            ChatState(
                info = info,
                inputMessageBuffer = inputMessageBuffer,
                editMessageBuffer = editMessageBuffer,
                editMessageId = editMessageId,
                status = messagingStatus.toImmutable(),
                openUriRequestDialog = openUriRequest,
                uriToOpen = uriToOpen,
                chatAppearance = chatAppearance
            )
        }

        viewModelScope.launch {
            stateFlow.collect {
                _state.value = it
            }
        }
    }

    private fun loadCard(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        getCharacterCard(id).collect {
            characterCard.value = it
            displayCard.value = it.toDisplayCard()
        }
    }

    private fun loadPersonasNames(personas: Flow<List<Persona>>) = personas.map { list ->
        list.associate { Pair(it.id, it.name) }
    }

    private fun loadPersonasImages(personas: Flow<List<Persona>>) = personas.map { list ->
        list.associate { persona ->
            Pair(persona.id, persona.imageName)
        }
    }


    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.UpdateInputMessage -> onUpdateInputMessage(event.message)
            is UserEvent.MessageSwipeLeft -> onMessageSwipeLeft(event.messageId)
            is UserEvent.MessageSwipeRight -> onMessageSwipeRight(event.messageId)
            UserEvent.MessageButtonClicked -> onMessageButtonClicked()
            is UserEvent.DeleteMessage -> onDeleteMessage(event.messageId)
            is UserEvent.EditMessage -> onEditMessage(event.messageId)
            UserEvent.SaveEditedMessage -> onSaveEditedMessage()
            UserEvent.DismissEditedMessage -> onDismissEditedMessage()
            is UserEvent.UpdateEditedMessage -> onUpdateEditedMessage(event.text)
            is UserEvent.MoveMessageDown -> onMoveMessageDown(event.messageId)
            is UserEvent.MoveMessageUp -> onMoveMessageUp(event.messageId)
            UserEvent.OpenChatList -> onOpenChatList()
            is UserEvent.BranchFromMessage -> onBranchFromMessage(event.messageId)
            is UserEvent.RestoreMessage -> onRestoreMessage(event.messageId)
            UserEvent.ScrollDown -> onScrollDown()
            is UserEvent.DeleteCurrentSwipe -> onDeleteCurrentSwipe(event.messageId)
            is UserEvent.RestoreSwipe -> onRestoreSwipe(event.messageId, event.swipeId)
            is UserEvent.OpenUriRequest -> onOpenUriRequest(event.uri)
            UserEvent.AcceptOpenUriRequest -> onAcceptOpenUriRequest()
            UserEvent.DismissOpenUriRequest -> onDismissOpenUriRequest()
        }
    }

    private fun onDismissOpenUriRequest() {
        savedStateHandle[OPEN_URI_REQUEST] = false
        savedStateHandle[URI_TO_OPEN] = ""
    }

    private fun onAcceptOpenUriRequest() {
        savedStateHandle[OPEN_URI_REQUEST] = false
        savedStateHandle.get<String>(URI_TO_OPEN)?.let {
            _uiEvent.value = Event(UiEvent.OpenUri(it))
        }
    }

    private fun onOpenUriRequest(uri: String) {
        savedStateHandle[OPEN_URI_REQUEST] = true
        savedStateHandle[URI_TO_OPEN] = uri
    }

    private fun onRestoreSwipe(messageId: Long, swipeId: Long) = viewModelScope.launch {
        restoreSwipe(messageId, swipeId)
    }

    private fun onDeleteCurrentSwipe(messageId: Long) = viewModelScope.launch {
        val deletionResult = deleteCurrentSwipe(messageId).valueOr {
            return@launch
        }
        _uiEvent.value = Event(
            UiEvent.RestoreSwipe(
                messageId = messageId,
                swipeId = deletionResult.deletedSwipeId,
            )
        )
    }

    private fun onScrollDown() {
        _uiEvent.value = Event(UiEvent.ScrollDown)
    }

    private fun onRestoreMessage(messageId: Long) = viewModelScope.launch {
        restoreMessage(messageId)
    }

    private fun onBranchFromMessage(messageId: Long) = viewModelScope.launch {
        val s = _state.value

        branchChatFromMessage(
            chatId = s.info.fullChat.chatId,
            messageId = messageId
        )
    }

    private fun onOpenChatList() {
        _uiEvent.value = Event(UiEvent.OpenChatList)
    }

    private fun onMoveMessageUp(messageId: Long) = viewModelScope.launch {
        val s = _state.value

        moveMessage(
            chatId = s.info.fullChat.chatId,
            messageId = messageId,
            direction = MoveMessageUseCase.Direction.Up
        )
    }

    private fun onMoveMessageDown(messageId: Long) = viewModelScope.launch {
        val s = _state.value

        moveMessage(
            chatId = s.info.fullChat.chatId,
            messageId = messageId,
            direction = MoveMessageUseCase.Direction.Down
        )
    }

    private fun onUpdateEditedMessage(text: String) {
        savedStateHandle[MESSAGE_EDIT_BUFFER] = text
    }

    private fun onDismissEditedMessage() {
        savedStateHandle[EDITED_MESSAGE_ID] = EMPTY_EDITED_MESSAGE_ID
    }

    private fun onSaveEditedMessage() = viewModelScope.launch {
        val id = savedStateHandle.get<Long>(EDITED_MESSAGE_ID) ?: return@launch
        val text = savedStateHandle.get<String>(MESSAGE_EDIT_BUFFER) ?: return@launch
        savedStateHandle[EDITED_MESSAGE_ID] = EMPTY_EDITED_MESSAGE_ID
        editMessage(id, text)
    }

    private fun onEditMessage(messageId: Long) {
        if (messageId < 1L) return

        val s = state.value

        s.info.fullChat.messages.find { it.messageId == messageId }?.let {
            savedStateHandle[MESSAGE_EDIT_BUFFER] = it.text
        }

        savedStateHandle[EDITED_MESSAGE_ID] = messageId
    }

    private fun onMessageButtonClicked() {
        val s = _state.value

        when (s.status) {
            ImmutableMessagingIndicator.None -> onSendMessage(s)
            else -> onCancelMessage()
        }
    }

    private fun onDeleteMessage(messageId: Long) = viewModelScope.launch {
        val s = _state.value
        if (messageId > 0) {
            deleteMessage(chatId = s.info.fullChat.chatId, messageId = messageId)
        }
        _uiEvent.value = Event(UiEvent.RestoreMessage(messageId))
    }

    private fun onCancelMessage() = viewModelScope.launch {
        cancelMessage()
    }

    private fun onSendMessage(state: ChatState) = viewModelScope.launch {
        val text = state.inputMessageBuffer
        savedStateHandle[MESSAGE_INPUT_BUFFER] = ""
        sendMessage(
            chatId = characterCard.value.selectedChat,
            personaId = state.info.fullChat.selectedPersonaId,
            text = text,
        )

        _uiEvent.value = Event(UiEvent.ScrollDown)
    }

    private fun onUpdateInputMessage(message: String) {
        savedStateHandle[MESSAGE_INPUT_BUFFER] = message
    }

    private fun onMessageSwipeLeft(messageId: Long) = viewModelScope.launch {
        onMessageSwipe(messageId, SwipeDirection.Left)
    }

    private fun onMessageSwipeRight(messageId: Long) = viewModelScope.launch {
        onMessageSwipe(messageId, SwipeDirection.Right)
    }

    private suspend fun onMessageSwipe(messageId: Long, direction: SwipeDirection) {
        val s = _state.value
        if (messageId == 0L) {
            swipeFirstMessage(
                cardId = characterCard.value.id,
                chatId = s.info.fullChat.chatId,
                direction = direction
            )
        } else {
            swipeMessage(
                messageId = messageId,
                direction = direction
            )
        }
    }

    companion object {
        private const val MESSAGE_INPUT_BUFFER = "MESSAGE_INPUT_BUFFER"
        private const val MESSAGE_EDIT_BUFFER = "MESSAGE_EDIT_BUFFER"
        private const val EDITED_MESSAGE_ID = "EDITED_MESSAGE"
        private const val OPEN_URI_REQUEST = "OPEN_URI_REQUEST"
        private const val URI_TO_OPEN = "URI_TO_OPEN"
        private const val EMPTY_EDITED_MESSAGE_ID = -1L
    }
}