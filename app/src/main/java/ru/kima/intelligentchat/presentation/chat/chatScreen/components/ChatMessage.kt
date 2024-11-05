package ru.kima.intelligentchat.presentation.chat.chatScreen.components

import android.icu.text.DateFormat
import android.icu.util.Calendar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.formatAndTrim
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.components.CardImage
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ChatDefaults
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayMessage
import ru.kima.intelligentchat.presentation.common.image.ImmutableImageBitmap
import ru.kima.intelligentchat.presentation.common.image.rememberVectorPainter
import ru.kima.intelligentchat.presentation.common.markdown.MarkdownText
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropDownMenuItem
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropdownMenu
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

enum class ChatMessageState {
    Common,
    Arrows,
    Edit,
}

@Composable
fun ChatMessage(
    message: DisplayMessage,
    editStateBuffer: String?,
    modifier: Modifier = Modifier,
    imageSize: Dp = ChatDefaults.SENDER_IMAGE_SIZE,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onMoveUpClicked: () -> Unit,
    onMoveDownClicked: () -> Unit,
    onImageClicked: () -> Unit,
    onLeftClicked: () -> Unit,
    onRightClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        ImageAndMetaInfo(
            imageBitmap = message.senderImage,
            index = message.index,
            showLeftArrow = message.state == ChatMessageState.Arrows,
            imageSize = imageSize,
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.TopStart),
            tookMs = 0,
            onImageClick = onImageClicked,
            onLeftClick = onLeftClicked
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(
                    start = imageSize + 8.dp, end =
                    if (message.state == ChatMessageState.Arrows) 56.dp
                    else 0.dp
                ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NameAndTime(
                name = message.senderName,
                sentTimeMillis = message.sentTimeMillis
            )


            TextArea(
                editState = message.state == ChatMessageState.Edit,
                editStateBuffer = editStateBuffer,
                solidText = message.text,
                currentSwipe = message.currentSwipe,
            )
        }

        SimpleDropdownMenu(
            dropdownMenuItems(
                onEditClicked = onEditClicked,
                onDeleteClicked = onDeleteClicked,
                onMoveUpClicked = onMoveUpClicked,
                onMoveDownClicked = onMoveDownClicked
            ),
            modifier = Modifier.align(Alignment.TopEnd)
        )

        if (message.state == ChatMessageState.Arrows) {
            RightArrow(
                currentSwipe = message.currentSwipe,
                totalSwipes = message.totalSwipes,
                modifier = Modifier.align(Alignment.BottomEnd),
                onRightClick = onRightClicked
            )
        }
    }
}

@Composable
private fun dropdownMenuItems(
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onMoveUpClicked: () -> Unit,
    onMoveDownClicked: () -> Unit
) = remember {
    listOf(
        SimpleDropDownMenuItem(
            textId = R.string.menu_item_edit_message,
            onClick = onEditClicked,
            iconVector = Icons.Default.Edit
        ),
        SimpleDropDownMenuItem(
            textId = R.string.menu_item_delete_message,
            onClick = onDeleteClicked,
            iconVector = Icons.Default.Delete
        ),
        SimpleDropDownMenuItem(
            textId = R.string.menu_item_move_message_up,
            onClick = onMoveUpClicked,
            iconVector = Icons.Default.ArrowUpward
        ),
        SimpleDropDownMenuItem(
            textId = R.string.menu_item_move_message_down,
            onClick = onMoveDownClicked,
            iconVector = Icons.Default.ArrowDownward
        ),
    )
}

@Composable
fun TextArea(
    editState: Boolean,
    editStateBuffer: String?,
    solidText: String,
    currentSwipe: Int,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        editState,
        modifier = modifier, label = ""
    ) { e ->
        when (e) {
            true -> TODO()
            false -> AnimatedText(
                text = solidText,
                currentSwipe = currentSwipe,
            )
        }
    }
}

@Composable
fun AnimatedText(
    text: String,
    currentSwipe: Int,
    modifier: Modifier = Modifier
) {
    val swipeRight = remember {
        slideIn { IntOffset(it.width, 0) } togetherWith slideOut { IntOffset(-it.width, 0) }
    }

    val swipeLeft = remember {
        slideIn { IntOffset(-it.width, 0) } togetherWith slideOut { IntOffset(it.width, 0) }
    }

    val calm = remember {
        fadeIn(tween(0)) togetherWith fadeOut(tween(0))
    }

    var prevSwipe by remember { mutableIntStateOf(0) }
    var animationSpec by remember { mutableStateOf(calm) }
    if (prevSwipe == 0) {
        prevSwipe = currentSwipe
    }

    animationSpec = if (prevSwipe == 0 || prevSwipe == currentSwipe)
        calm
    else if (currentSwipe > prevSwipe) {
        swipeRight
    } else {
        swipeLeft
    }
    prevSwipe = currentSwipe

    AnimatedContent(
        targetState = text, label = "",
        modifier = modifier,
        transitionSpec = { animationSpec }) {
        MarkdownText(
            text = it,
//                style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun ImageAndMetaInfo(
    imageBitmap: ImmutableImageBitmap,
    index: Int,
    showLeftArrow: Boolean,
    imageSize: Dp,
    modifier: Modifier = Modifier,
    tookMs: Long = 0,
    onImageClick: () -> Unit,
    onLeftClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            MessageSenderImage(
                imageBitmap = imageBitmap,
                modifier = Modifier.size(imageSize),
                onClick = onImageClick
            )

            Text(
                text = "#$index",
                style = MaterialTheme.typography.bodySmall
            )

            if (tookMs > 0L) {
                val seconds = (tookMs / 1000f).formatAndTrim(2)
                Text(
                    text = "${seconds}S",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        if (showLeftArrow) {
            IconButton(
                onClick = onLeftClick,
                modifier = Modifier.size(ChatDefaults.SENDER_IMAGE_SIZE)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowLeft, contentDescription = "")
            }
        }
    }
}

@Composable
fun NameAndTime(
    name: String,
    sentTimeMillis: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.alignByBaseline()
        )

        val date = remember(sentTimeMillis) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = sentTimeMillis
            val format = DateFormat.getInstance()
            format.format(calendar.time)
        }

        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignByBaseline()
        )

        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun RightArrow(
    currentSwipe: Int,
    totalSwipes: Int,
    modifier: Modifier = Modifier,
    onRightClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            text = "${currentSwipe}/${totalSwipes}",
            style = MaterialTheme.typography.bodySmall
        )
        IconButton(onClick = onRightClick) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowRight, contentDescription = "")
        }
    }
}

@Composable
fun MessageSenderImage(
    imageBitmap: ImmutableImageBitmap,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    CardImage(
        bitmap = imageBitmap,
        modifier = modifier, emptyPainter = rememberVectorPainter(
            image = Icons.Default.QuestionMark,
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        onClick = onClick
    )
}

@Preview
@Composable
private fun ChatMessagePreview() {
    IntelligentChatTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ChatMessage(
                message = DisplayMessage(
                    messageId = 0,
                    senderName = "Sender",
                    text = "Message Text Long Enough To Wrap Around The Line"
                ),
                editStateBuffer = null,
                modifier = Modifier.padding(8.dp),
                onImageClicked = {},
                onLeftClicked = {},
                onRightClicked = {},
                onEditClicked = {},
                onDeleteClicked = {},
                onMoveUpClicked = {},
                onMoveDownClicked = {}
            )
        }
    }
}

@Preview
@Composable
private fun ChatMessageWithSwipesPreview() {
    IntelligentChatTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ChatMessage(
                message = DisplayMessage(
                    messageId = 0,
                    senderName = "Sender",
                    text = "Message Text Long Enough To Wrap Around The Line",
                    currentSwipe = 2,
                    totalSwipes = 2,
                    state = ChatMessageState.Arrows
                ),
                editStateBuffer = null,
                modifier = Modifier.padding(8.dp),
                onImageClicked = {},
                onLeftClicked = {},
                onRightClicked = {},
                onEditClicked = {},
                onDeleteClicked = {},
                onMoveUpClicked = {},
                onMoveDownClicked = {},
            )
        }
    }
}

@Preview
@Composable
private fun ChatMessageEditStatePreview() {
    IntelligentChatTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            ChatMessage(
                message = DisplayMessage(
                    messageId = 0,
                    senderName = "Sender",
                    text = "Message Text Long Enough To Wrap Around The Line",
                    currentSwipe = 2,
                    totalSwipes = 2,
                    state = ChatMessageState.Edit
                ),
                editStateBuffer = "Message Text Long Enough To Wrap Around The Line",
                modifier = Modifier.padding(8.dp),
                onImageClicked = {},
                onLeftClicked = {},
                onRightClicked = {},
                onEditClicked = {},
                onDeleteClicked = {},
                onMoveUpClicked = {},
                onMoveDownClicked = {},
            )
        }
    }
}