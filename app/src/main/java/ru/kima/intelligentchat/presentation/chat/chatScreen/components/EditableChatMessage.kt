package ru.kima.intelligentchat.presentation.chat.chatScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ChatDefaults
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayMessage
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun EditableChatMessage(
    message: DisplayMessage,
    buffer: String,
    onType: (String) -> Unit,
    onSaveClick: () -> Unit,
    onDismissClick: () -> Unit,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageSize: Dp = ChatDefaults.SENDER_IMAGE_SIZE,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ImageAndMetaInfo(
            imageBitmap = message.senderImage,
            index = message.index,
            showLeftArrow = false,
            imageSize = imageSize,
            modifier = Modifier.fillMaxHeight(),
            tookMs = 0,
            onImageClick = onImageClick,
            onLeftClick = {}
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = message.senderName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.alignByBaseline()
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onSaveClick) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null)
                }
                IconButton(onClick = onDismissClick) {
                    Icon(imageVector = Icons.Default.Cancel, contentDescription = null)
                }
            }
            OutlinedTextField(
                value = buffer, onValueChange = onType,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun EditableChatMessagePreview() {
    IntelligentChatTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            EditableChatMessage(
                message = DisplayMessage(
                    messageId = 0,
                    senderName = "Sender",
                    text = "Message Text Long Enough To Wrap Around The Line"
                ),
                buffer = "Message Text Long Enough To Wrap Around The Line",
                modifier = Modifier.padding(8.dp),
                onType = {},
                onSaveClick = {},
                onDismissClick = {},
                onImageClick = {},
            )
        }
    }
}