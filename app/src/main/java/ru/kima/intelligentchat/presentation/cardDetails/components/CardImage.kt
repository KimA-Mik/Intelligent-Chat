package ru.kima.intelligentchat.presentation.cardDetails.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person4
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun CardImage(
    photoBytes: ByteArray?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val bitmap = remember(photoBytes) {
        if (photoBytes != null)
            BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.size).asImageBitmap()
        else
            null
    }
    if (bitmap == null) {
        val iconBgColor = MaterialTheme.colorScheme.primaryContainer
        Icon(
            Icons.Filled.Person4,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = modifier
                .clip(CircleShape)
                .border(
                    1.5.dp,
                    MaterialTheme.colorScheme.onPrimaryContainer,
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
        Image(
            painter = BitmapPainter(bitmap), contentDescription = "",
            modifier = modifier
                .clip(CircleShape)
                .border(
                    1.5.dp,
                    MaterialTheme.colorScheme.onPrimaryContainer,
                    CircleShape
                )
                .clickable {
                    onClick()
                },
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
            CardImage(photoBytes = null) {

            }
        }
    }
}