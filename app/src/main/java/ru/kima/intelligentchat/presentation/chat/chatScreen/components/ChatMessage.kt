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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.AltRoute
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.highlightedCodeBlock
import com.mikepenz.markdown.compose.elements.highlightedCodeFence
import com.mikepenz.markdown.compose.extendedspans.ExtendedSpans
import com.mikepenz.markdown.compose.extendedspans.RoundedCornerSpanPainter
import com.mikepenz.markdown.compose.extendedspans.SquigglyUnderlineSpanPainter
import com.mikepenz.markdown.compose.extendedspans.rememberSquigglyUnderlineAnimator
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.markdownAnnotator
import com.mikepenz.markdown.model.markdownExtendedSpans
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.common.formatAndTrim
import ru.kima.intelligentchat.presentation.characterCard.cardDetails.components.AsyncCardImage
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.ChatDefaults
import ru.kima.intelligentchat.presentation.chat.chatScreen.model.DisplayMessage
import ru.kima.intelligentchat.presentation.common.components.conditional
import ru.kima.intelligentchat.presentation.common.image.rememberVectorPainter
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropDownMenuItem
import ru.kima.intelligentchat.presentation.ui.components.SimpleDropdownMenu
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun ChatMessage(
    message: DisplayMessage,
    modifier: Modifier = Modifier,
    imageSize: Dp = ChatDefaults.SENDER_IMAGE_SIZE,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onDeleteSwipeClicked: () -> Unit,
    onMoveUpClicked: () -> Unit,
    onMoveDownClicked: () -> Unit,
    onBranchChatClicked: () -> Unit,
    onImageClicked: () -> Unit,
    onLeftClicked: () -> Unit,
    onRightClicked: () -> Unit
) = Column(modifier = modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        ImageAndMetaInfo(
            photoName = message.senderImageName,
            index = message.index,
            imageSize = imageSize,
            tookMs = 0,
            onImageClick = onImageClicked,
        )

        Text(
            text = message.senderName,
            style = MaterialTheme.typography.headlineSmall,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .alignByBaseline()
                .weight(1f)
        )

        if (message.index > 0) {
            val date = remember(message.sentTimeMillis) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = message.sentTimeMillis
                val format = DateFormat.getInstance()
                format.format(calendar.time)
            }
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .alignByBaseline()
                    .width(IntrinsicSize.Min)
            )

            SimpleDropdownMenu(
                dropdownMenuItems(
                    addDeleteSwipe = message.showSwipeInfo && message.totalSwipes > 1,
                    onEditClicked = onEditClicked,
                    onDeleteClicked = onDeleteClicked,
                    onDeleteSwipeClicked = onDeleteSwipeClicked,
                    onMoveUpClicked = onMoveUpClicked,
                    onMoveDownClicked = onMoveDownClicked,
                    onBranchChatClicked = onBranchChatClicked
                )
            )
        }
    }

    Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
    ) {
        if (message.showSwipeInfo) {
            IconButton(
                onClick = onLeftClicked,
                modifier = Modifier
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowLeft, contentDescription = "")
            }
        }

        AnimatedText(
            text = message.text,
            currentSwipe = message.currentSwipe,
            modifier = Modifier
                .weight(1f)
                .conditional(!message.showSwipeInfo) { padding(horizontal = 8.dp) }
        )

        if (message.showSwipeInfo) {
            RightArrow(
                currentSwipe = message.currentSwipe,
                totalSwipes = message.totalSwipes,
                onRightClick = onRightClicked
            )
        }
    }
}

@Composable
private fun dropdownMenuItems(
    addDeleteSwipe: Boolean,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    onDeleteSwipeClicked: () -> Unit,
    onMoveUpClicked: () -> Unit,
    onMoveDownClicked: () -> Unit,
    onBranchChatClicked: () -> Unit
) = remember(addDeleteSwipe) {
    buildList {
        add(
            SimpleDropDownMenuItem(
                textId = R.string.menu_item_edit_message,
                onClick = onEditClicked,
                iconVector = Icons.Default.Edit
            )
        )

        add(
            SimpleDropDownMenuItem(
                textId = R.string.menu_item_delete_message,
                onClick = onDeleteClicked,
                iconVector = Icons.Default.Delete
            )
        )

        if (addDeleteSwipe)
            add(
                SimpleDropDownMenuItem(
                    textId = R.string.menu_item_delete_swipe,
                    onClick = onDeleteSwipeClicked,
                    iconVector = Icons.Default.DeleteSweep
                )
            )

        add(
            SimpleDropDownMenuItem(
                textId = R.string.menu_item_move_message_up,
                onClick = onMoveUpClicked,
                iconVector = Icons.Default.ArrowUpward
            )
        )

        add(
            SimpleDropDownMenuItem(
                textId = R.string.menu_item_move_message_down,
                onClick = onMoveDownClicked,
                iconVector = Icons.Default.ArrowDownward
            )
        )

        add(
            SimpleDropDownMenuItem(
                textId = R.string.menu_item_branch_chat,
                onClick = onBranchChatClicked,
                iconVector = Icons.AutoMirrored.Default.AltRoute
            )
        )
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

    val colorScheme = MaterialTheme.colorScheme
    //TODO: Factor out colors
    val italicStyle = remember {
        SpanStyle(
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onPrimaryContainer
        )
    }
    val quoteStyle = remember {
        SpanStyle(
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            color = colorScheme.primary
        )
    }

    var italicIndex by remember(text) { mutableIntStateOf(-1) }
    var quoteIndex by remember(text) { mutableIntStateOf(-1) }
    var parent by remember(text) { mutableStateOf<ASTNode?>(null) }
    AnimatedContent(
        targetState = text, label = "",
        modifier = modifier,
        transitionSpec = { animationSpec }) {
        Markdown(
            it,
            colors = markdownColor(),
            typography = markdownTypography(),
            flavour = CommonMarkFlavourDescriptor(),
            extendedSpans = markdownExtendedSpans {
                val animator = rememberSquigglyUnderlineAnimator()
                remember {
                    ExtendedSpans(
                        RoundedCornerSpanPainter(),
                        SquigglyUnderlineSpanPainter(animator = animator),
                    )
                }
            },
            annotator = markdownAnnotator { _, child ->
                if (child.parent != parent) {
                    parent = child.parent
                    italicIndex = -1
                    quoteIndex = -1
                }

                when {
                    child.type == MarkdownTokenTypes.EMPH && child.endOffset - child.startOffset == 1 -> {
                        if (italicIndex < 0) {
                            italicIndex = pushStyle(italicStyle)
                        } else {
                            pop(italicIndex)
                            quoteIndex = shiftIfNeeded(italicIndex, quoteIndex)
                            italicIndex = -1
                        }
                    }

                    child.type == MarkdownTokenTypes.DOUBLE_QUOTE -> {
                        if (quoteIndex < 0) {
                            quoteIndex = pushStyle(quoteStyle)
                        } else {
                            append(MarkdownTokenTypes.DOUBLE_QUOTE.name)
                            pop(quoteIndex)
                            italicIndex = shiftIfNeeded(quoteIndex, italicIndex)
                            quoteIndex = -1
                            return@markdownAnnotator true
                        }
                    }
                }

                return@markdownAnnotator false
            },
            components = markdownComponents(
                codeBlock = highlightedCodeBlock,
                codeFence = highlightedCodeFence,
            )
        )
    }
}

fun shiftIfNeeded(l: Int, r: Int): Int {
    return if (l <= r) -1
    else r
}

@Composable
fun ImageAndMetaInfo(
    photoName: String?,
    index: Int,
    imageSize: Dp,
    modifier: Modifier = Modifier,
    tookMs: Long = 0,
    onImageClick: () -> Unit,
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
                photoName = photoName,
                imageSize = imageSize,
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
    photoName: String?, imageSize: Dp, modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    AsyncCardImage(
        photoName = photoName,
        modifier = modifier, emptyPainter = rememberVectorPainter(
            image = Icons.Default.QuestionMark,
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        imageSize = imageSize,
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
                    senderName = "Very Longggggggggg Sender Name",
                    text = "Message Text Long Enough To Wrap Around The Line"
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onImageClicked = {},
                onLeftClicked = {},
                onRightClicked = {},
                onEditClicked = {},
                onDeleteClicked = {},
                onDeleteSwipeClicked = {},
                onMoveUpClicked = {},
                onMoveDownClicked = {},
                onBranchChatClicked = {}
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
                    senderName = "Very Long Sender Name",
                    text = "Message Text Long Enough To Wrap Around The Line",
                    currentSwipe = 2,
                    totalSwipes = 2,
                    showSwipeInfo = true
                ),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                onImageClicked = {},
                onLeftClicked = {},
                onRightClicked = {},
                onEditClicked = {},
                onDeleteClicked = {},
                onDeleteSwipeClicked = {},
                onMoveUpClicked = {},
                onMoveDownClicked = {},
                onBranchChatClicked = {}
            )
        }
    }
}