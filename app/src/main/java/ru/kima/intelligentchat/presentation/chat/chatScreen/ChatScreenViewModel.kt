package ru.kima.intelligentchat.presentation.chat.chatScreen

import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.useCase.CreateAndSelectChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToCardChatUseCase
import ru.kima.intelligentchat.domain.persona.model.Persona
import ru.kima.intelligentchat.domain.persona.useCase.GetPersonasUseCase
import ru.kima.intelligentchat.domain.persona.useCase.LoadPersonaImageUseCase
import ru.kima.intelligentchat.presentation.chat.chatScreen.events.UserEvent
import ru.kima.intelligentchat.presentation.chat.chatScreen.mappers.toDisplayCard
import ru.kima.intelligentchat.presentation.chat.chatScreen.mappers.toDisplayChat
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayCard
import ru.kima.intelligentchat.presentation.common.image.ImmutableImageBitmap
import ru.kima.intelligentchat.presentation.navigation.graphs.CARD_ID_ARGUMENT

class ChatScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getCharacterCard: GetCardUseCase,
    private val createAndSelectChat: CreateAndSelectChatUseCase,
    private val subscribeToCardChat: SubscribeToCardChatUseCase,
    private val getPersonas: GetPersonasUseCase,
    private val loadPersonaImage: LoadPersonaImageUseCase
) : ViewModel() {
    private val characterCard = MutableStateFlow(CharacterCard())
    private val displayCard = MutableStateFlow(DisplayCard())

    private val _state = MutableStateFlow<ChatScreenState>(ChatScreenState.ChatState())
    val state = _state.asStateFlow()

    init {
        initialize()
    }

    private fun initialize() {
        val id = savedStateHandle.get<Long>(CARD_ID_ARGUMENT)
        if (id == null || id == 0L) {
            _state.value = ChatScreenState.ErrorState
            return
        }

        loadCard(id)


        val chat = subscribeToCardChat(id)
            .map {
                when (it) {
                    SubscribeToCardChatUseCase.Result.NotFound -> FullChat()
                    is SubscribeToCardChatUseCase.Result.Success -> it.fullChat
                    SubscribeToCardChatUseCase.Result.UnknownError -> FullChat()
                }
            }

        val personas = getPersonas()
        val personasNames = loadPersonasNames(personas)
        val personasImages = loadPersonasImages(personas)

        val chatInfo = combine(
            characterCard, displayCard, chat, personasNames, personasImages
        ) { characterCard, displayCard, fullChat, names, images ->
            ChatScreenState.ChatState.ChatInfo(
                characterCard = displayCard,
                fullChat = fullChat.toDisplayChat(
                    card = characterCard,
                    preloadCardImageBitmap = displayCard.image,
                    personasNames = names,
                    personasImages = images
                )
            )
        }

        val stateFlow = combine(
            chatInfo,
            savedStateHandle.getStateFlow(MESSAGE_INPUT_BUFFER, String()),
        ) { info, inputMessageBuffer ->
            ChatScreenState.ChatState(
                info = info,
                inputMessageBuffer = inputMessageBuffer,
            )
        }

        viewModelScope.launch {
            stateFlow.collect {
                _state.value = it
            }
        }
    }

    private fun loadCard(id: Long) = viewModelScope.launch {
        getCharacterCard(id).collect {
            if (it.selectedChat == 0L) {
                createAndSelectChat(it)
            }
            characterCard.value = it
            displayCard.value = it.toDisplayCard()
        }
    }

    private fun loadPersonasNames(personas: Flow<List<Persona>>) = personas.map { list ->
        list.associate { Pair(it.id, it.name) }
    }

    private fun loadPersonasImages(personas: Flow<List<Persona>>) = personas.map { list ->
        list.associate { persona ->
            val image = persona.imageName?.let {
                loadPersonaImage(persona.id)
            }

            val imageBitmap = image?.bitmap?.asImageBitmap()
            imageBitmap?.prepareToDraw()

            Pair(persona.id, ImmutableImageBitmap(imageBitmap))
        }
    }


    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.UpdateInputMessage -> onUpdateInputMessage(event.message)
        }
    }

    private fun onUpdateInputMessage(message: String) {
        savedStateHandle[MESSAGE_INPUT_BUFFER] = message
    }

    companion object {
        private const val MESSAGE_INPUT_BUFFER = "MESSAGE_INPUT_BUFFER"
    }
}