package ru.kima.intelligentchat.presentation.chat.chatScreen.mappers

import ru.kima.intelligentchat.domain.card.model.AltGreeting
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.domain.chat.model.FullChat
import ru.kima.intelligentchat.domain.chat.model.MessageWithSwipes
import ru.kima.intelligentchat.domain.chat.model.SenderType
import ru.kima.intelligentchat.presentation.chat.chatScreen.components.ChatMessageState
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayChat
import ru.kima.intelligentchat.presentation.common.image.ImmutableImageBitmap

fun FullChat.toDisplayChat(
    card: CharacterCard,
    preloadCardImageBitmap: ImmutableImageBitmap,
    selectedPersona: Long,
    editedMessageId: Long,
    personasNames: Map<Long, String> = emptyMap(),
    personasImages: Map<Long, ImmutableImageBitmap> = emptyMap()
): DisplayChat {
    val chatMessages = if (messages.isEmpty()) {
        val firstMessage = MessageWithSwipes(
            messageId = 0,
            chatId = 0,
            sender = SenderType.Character,
            senderId = card.id,
            index = 0,
            selectedSwipeIndex = selectedGreeting,
            swipes = card.alternateGreetings.toMutableList()
                .apply { add(0, AltGreeting(id = 0, body = card.firstMes)) }
                .map { it.toSwipe(0) }
                .map {
                    it.copy(
                        text = it.text
                            .replace("{{char}}", card.name, true)
                            .let { body ->
                                val personaName = personasNames[selectedPersona] ?: return@let body
                                body.replace("{{user}}", personaName, true)
                            }
                    )
                },
            deleted = false
        )

        mutableListOf(
            firstMessage.toDisplayMessage(
                card.name,
                preloadCardImageBitmap,
                personasNames,
                personasImages,
                state = if (firstMessage.swipes.size > 1) ChatMessageState.Arrows else ChatMessageState.Common
            )
        )
    } else {
        messages.mapIndexed { index, message ->
            message.toDisplayMessage(
                card.name, preloadCardImageBitmap, personasNames, personasImages,
                state = if (message.messageId == editedMessageId) ChatMessageState.Edit
                else if (index == messages.lastIndex && message.sender == SenderType.Character) ChatMessageState.Arrows
                else ChatMessageState.Common
            )
        }
    }

    return DisplayChat(
        chatId = chatId,
        selectedPersonaId = selectedPersona,
        messages = chatMessages
    )
}