package ru.kima.intelligentchat.data.chat.mappers

import ru.kima.intelligentchat.data.chat.dto.MessageWithSwipesDto
import ru.kima.intelligentchat.data.chat.entities.MessageEntity
import ru.kima.intelligentchat.data.chat.entities.SwipeEntity
import ru.kima.intelligentchat.domain.chat.model.Message
import ru.kima.intelligentchat.domain.chat.model.MessageWithSwipes
import ru.kima.intelligentchat.domain.chat.model.Swipe

fun MessageWithSwipesDto.toEntity(): MessageWithSwipes {
    return MessageWithSwipes(
        messageId = message.messageId,
        chatId = message.chatId,
        sender = message.sender.toSenderType(),
        senderId = message.senderId,
        index = message.index,
        selectedSwipeIndex = message.selectedSwipeIndex,
        swipes = swipes.map(SwipeEntity::toSwipe),
        deleted = message.deleted
    )
}

fun MessageWithSwipes.toDto(): MessageWithSwipesDto {
    return MessageWithSwipesDto(
        message = MessageEntity(
            messageId = messageId,
            chatId = chatId,
            sender = sender.toDto(),
            senderId = senderId,
            index = index,
            selectedSwipeIndex = selectedSwipeIndex,
            deleted = deleted
        ),
        swipes = swipes.map(Swipe::toEntity)
    )
}

fun MessageEntity.toEntity(): Message {
    return Message(
        messageId = messageId,
        chatId = chatId,
        sender = sender.toSenderType(),
        senderId = senderId,
        index = index,
        selectedSwipeIndex = selectedSwipeIndex,
        deleted = deleted,
    )
}

fun Message.toEntity(): MessageEntity {
    return MessageEntity(
        messageId = messageId,
        chatId = chatId,
        sender = sender.toDto(),
        senderId = senderId,
        index = index,
        selectedSwipeIndex = selectedSwipeIndex,
        deleted = deleted,
    )
}