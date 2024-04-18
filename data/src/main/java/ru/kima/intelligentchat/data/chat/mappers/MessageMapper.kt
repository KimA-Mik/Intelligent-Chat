package ru.kima.intelligentchat.data.chat.mappers

import ru.kima.intelligentchat.data.chat.dto.MessageWithSwipesDto
import ru.kima.intelligentchat.data.chat.entities.MessageEntity
import ru.kima.intelligentchat.data.chat.entities.SwipeEntity
import ru.kima.intelligentchat.domain.chat.model.Message
import ru.kima.intelligentchat.domain.chat.model.Swipe

fun MessageWithSwipesDto.toMessage(): Message {
    return Message(
        messageId = message.messageId,
        chatId = message.chatId,
        sender = message.sender.toSenderType(),
        senderId = message.senderId,
        index = message.index,
        selectedSwipeIndex = message.selectedSwipeIndex,
        swipes = swipes.map(SwipeEntity::toSwipe),
    )
}

fun Message.toDto(): MessageWithSwipesDto {
    return MessageWithSwipesDto(
        message = MessageEntity(
            messageId = messageId,
            chatId = chatId,
            sender = sender.toDto(),
            senderId = senderId,
            index = index,
            selectedSwipeIndex = selectedSwipeIndex,
        ),
        swipes = swipes.map(Swipe::toEntity)
    )
}