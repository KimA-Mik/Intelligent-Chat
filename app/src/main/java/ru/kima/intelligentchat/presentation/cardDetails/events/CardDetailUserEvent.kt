package ru.kima.intelligentchat.presentation.cardDetails.events

sealed interface CardDetailUserEvent {
    data class UpdateCardImage(val bytes: ByteArray) : CardDetailUserEvent
    data object SelectImageClicked : CardDetailUserEvent
}