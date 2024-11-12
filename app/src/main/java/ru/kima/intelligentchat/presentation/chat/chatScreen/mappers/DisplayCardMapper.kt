package ru.kima.intelligentchat.presentation.chat.chatScreen.mappers

import androidx.compose.ui.graphics.asImageBitmap
import ru.kima.intelligentchat.domain.card.model.CharacterCard
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayCard
import ru.kima.intelligentchat.presentation.common.image.ImmutableImageBitmap

fun CharacterCard.toDisplayCard(): DisplayCard {
    val imageBitmap = photoBytes?.asImageBitmap()
    imageBitmap?.prepareToDraw()
    return DisplayCard(
        id = id,
        name = name,
        image = ImmutableImageBitmap(imageBitmap)
    )
}