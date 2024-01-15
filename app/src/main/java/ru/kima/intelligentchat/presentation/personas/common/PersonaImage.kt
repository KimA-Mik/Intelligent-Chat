package ru.kima.intelligentchat.presentation.personas.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun PersonaImage(
    container: PersonaImageContainer?,
    modifier: Modifier = Modifier,
    border: Boolean = true,
    onClick: () -> Unit
) {
    var imageBitmap by remember {
        mutableStateOf<ImageBitmap?>(null)
    }

    LaunchedEffect(container) {
        imageBitmap = container?.bitmap?.asImageBitmap()
    }

    if (imageBitmap == null) {
        val iconBgColor = MaterialTheme.colorScheme.secondaryContainer
        Icon(
            Icons.Filled.Person3,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = modifier
                .clip(CircleShape)
                .border(
                    1.5.dp,
                    MaterialTheme.colorScheme.onSecondaryContainer,
                    CircleShape
                )
                .drawBehind {
                    drawCircle(color = iconBgColor)
                }
                .clickable {
                    onClick()
                }
        )
    } else {
        val m = if (border) {
            modifier
                .border(
                    1.5.dp,
                    MaterialTheme.colorScheme.onSecondaryContainer,
                    CircleShape
                )
        } else {
            modifier
        }

        Image(
            painter = BitmapPainter(imageBitmap!!), contentDescription = "",
            modifier = m
                .clip(CircleShape)
                .clickable {
                    onClick()
                },
            contentScale = ContentScale.Crop
        )
    }
}
