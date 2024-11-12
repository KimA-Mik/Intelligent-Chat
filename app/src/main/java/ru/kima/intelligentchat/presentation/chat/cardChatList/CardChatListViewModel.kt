package ru.kima.intelligentchat.presentation.chat.cardChatList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.common.Event
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.chat.model.ChatWithMessages
import ru.kima.intelligentchat.domain.chat.useCase.CreateChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.DeleteChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.RenameChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.SelectChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToCardChatsUseCase
import ru.kima.intelligentchat.presentation.chat.cardChatList.events.UiEvent
import ru.kima.intelligentchat.presentation.chat.cardChatList.events.UserEvent
import ru.kima.intelligentchat.presentation.chat.cardChatList.mappers.toListItem
import ru.kima.intelligentchat.presentation.chat.chatScreen.mappers.toDisplayCard
import ru.kima.intelligentchat.presentation.navigation.graphs.CARD_ID_ARGUMENT

class CardChatListViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val createChat: CreateChatUseCase,
    private val deleteChat: DeleteChatUseCase,
    private val renameChat: RenameChatUseCase,
    private val getCharacterCard: GetCardUseCase,
    private val selectChatUseCase: SelectChatUseCase,
    private val subscribeToCardChats: SubscribeToCardChatsUseCase,
) : ViewModel() {
    private val _uiEvent = MutableStateFlow(Event<UiEvent>(null))
    val uiEvent = _uiEvent.asStateFlow()

    private val card = MutableStateFlow(CharacterCard.default())
    private val chats = MutableStateFlow(emptyList<ChatWithMessages>())
    private val cardAndChats = combine(card, chats) { card, chats ->
        card.toDisplayCard() to chats.map { it.toListItem(selected = it.chat.chatId == card.selectedChat) }
    }

    val state = combine(
        cardAndChats,
        savedStateHandle.getStateFlow<Boolean>(DELETE_CHAT_DIALOG_KEY, false),
        savedStateHandle.getStateFlow<Boolean>(RENAME_CHAT_DIALOG_KEY, false),
        savedStateHandle.getStateFlow<String>(RENAME_CHAT_BUFFER_KEY, ""),
    ) { cardAndChats, deleteChatDialog, renameChatDialog, renameChatBuffer ->
        CardChatListState(
            displayCard = cardAndChats.first,
            chats = cardAndChats.second,
            deleteChatDialog = deleteChatDialog,
            renameChatDialog = renameChatDialog,
            renameChatBuffer = renameChatBuffer
        )
    }

    init {
        initialize()
    }

    private fun initialize() {
        val cardId = savedStateHandle.get<Long>(CARD_ID_ARGUMENT)
        if (cardId == null || cardId < 1) {
            _uiEvent.update { Event(UiEvent.IncorrectInitialization) }
            return
        }

        viewModelScope.launch {
            getCharacterCard(cardId).collect {
                card.value = it
            }
        }

        viewModelScope.launch {
            subscribeToCardChats(cardId).collect {
                chats.value = it
            }
        }
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            UserEvent.CreateChat -> onCreateChat()
            is UserEvent.DeleteChat -> onDeleteChat(event.chatId)
            is UserEvent.RenameChat -> onRenameChat(event.chatId)
            is UserEvent.SelectChat -> onSelectChat(event.chatId)
            UserEvent.DeleteChatAccept -> onDeleteChatAccept()
            UserEvent.DeleteChatDismiss -> onDeleteChatDismiss()
            UserEvent.RenameChatAccept -> onRenameChatAccept()
            UserEvent.RenameChatDismiss -> onRenameChatDismiss()
            is UserEvent.RenameChatUpdateBuffer -> onRenameChatUpdateBuffer(event.buffer)
        }
    }

    private fun onRenameChatUpdateBuffer(buffer: String) {
        savedStateHandle[RENAME_CHAT_BUFFER_KEY] = buffer
    }

    private fun onRenameChatDismiss() {
        savedStateHandle[RENAME_CHAT_DIALOG_KEY] = false
        savedStateHandle[RENAME_CHAT_BUFFER_KEY] = String()
        savedStateHandle[RENAME_CHAT_ID_KEY] = EMPTY_ID
    }

    private fun onRenameChatAccept() = viewModelScope.launch {
        savedStateHandle[RENAME_CHAT_DIALOG_KEY] = false

        val chatId = savedStateHandle.get<Long>(RENAME_CHAT_ID_KEY)
        if (chatId == null || chatId < 1L) return@launch
        val newTitle = savedStateHandle.get<String>(RENAME_CHAT_BUFFER_KEY) ?: return@launch

        renameChat(chatId, newTitle)

        savedStateHandle[RENAME_CHAT_BUFFER_KEY] = String()
        savedStateHandle[RENAME_CHAT_ID_KEY] = EMPTY_ID
    }

    private fun onDeleteChatDismiss() {
        savedStateHandle[DELETE_CHAT_DIALOG_KEY] = false
        savedStateHandle[DELETE_CHAT_ID_KEY] = EMPTY_ID
    }

    private fun onDeleteChatAccept() = viewModelScope.launch {
        savedStateHandle[DELETE_CHAT_DIALOG_KEY] = false
        val deleteChatId = savedStateHandle.get<Long>(DELETE_CHAT_ID_KEY) ?: return@launch
        if (deleteChatId < 1L) return@launch
        deleteChat(deleteChatId)
        savedStateHandle[DELETE_CHAT_ID_KEY] = EMPTY_ID
    }

    private fun onSelectChat(chatId: Long) = viewModelScope.launch {
        selectChatUseCase(
            chatId = chatId,
            cardId = card.value.id
        )
    }

    private fun onRenameChat(chatId: Long) {
        val chat = chats.value.find { it.chat.chatId == chatId } ?: return
        savedStateHandle[RENAME_CHAT_BUFFER_KEY] = chat.chat.title
        savedStateHandle[RENAME_CHAT_ID_KEY] = chatId
        savedStateHandle[RENAME_CHAT_DIALOG_KEY] = true
    }

    private fun onDeleteChat(chatId: Long) {
        savedStateHandle[DELETE_CHAT_ID_KEY] = chatId
        savedStateHandle[DELETE_CHAT_DIALOG_KEY] = true
    }

    private fun onCreateChat() = viewModelScope.launch {
        createChat(card.value.id)
    }

    companion object {
        private const val DELETE_CHAT_DIALOG_KEY = "delete_chat_dialog"
        private const val DELETE_CHAT_ID_KEY = "delete_chat_id"
        private const val RENAME_CHAT_DIALOG_KEY = "rename_chat_dialog"
        private const val RENAME_CHAT_BUFFER_KEY = "rename_chat_buffer"
        private const val RENAME_CHAT_ID_KEY = "rename_chat_id"
        private const val EMPTY_ID = 0L
    }
}