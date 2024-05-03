package ru.kima.intelligentchat.presentation.characterCard.cardDetails.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person4
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.presentation.common.image.ImmutableBitmap
import ru.kima.intelligentchat.presentation.common.image.ImmutableImageBitmap
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CardImage(
    image: ImmutableBitmap,
    modifier: Modifier = Modifier,
    emptyImageVector: ImageVector = Icons.Default.Person4,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    val bitmap by remember(image) {
        val imageBitmap = image.bitmap?.asImageBitmap()
        imageBitmap?.prepareToDraw()
        mutableStateOf(ImmutableImageBitmap(imageBitmap))
    }

    CardImage(
        bitmap = bitmap,
        modifier = modifier,
        emptyImageVector = emptyImageVector,
        contentDescription = contentDescription,
        onClick = onClick
    )
}

@Composable
fun CardImage(
    bitmap: ImmutableImageBitmap,
    modifier: Modifier = Modifier,
    emptyImageVector: ImageVector = Icons.Default.Person4,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    val imageModifier = modifier
        .clip(CircleShape)
        .border(
            1.5.dp,
            MaterialTheme.colorScheme.onSecondaryContainer,
            CircleShape
        )
        .clickable {
            onClick()
        }

    if (bitmap.imageBitmap == null) {
        val iconBgColor = MaterialTheme.colorScheme.secondaryContainer
        Icon(
            imageVector = emptyImageVector,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = imageModifier
                .drawBehind {
                    drawCircle(color = iconBgColor)
                }
        )
    } else {
        Image(
            painter = BitmapPainter(bitmap.imageBitmap),
            contentDescription = contentDescription,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(name = "CardImage preview light theme")
@Preview(
    name = "CardImage preview dark theme",
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun CardImagePreview() {
    IntelligentChatTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            CardImage(
                image = ImmutableBitmap(),
                modifier = Modifier.padding(8.dp),
                onClick = {}
            )
        }
    }
}