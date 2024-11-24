package ru.kima.intelligentchat.presentation.common.markdown

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownDimens
import com.mikepenz.markdown.compose.LocalMarkdownPadding
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.elements.MarkdownCodeBackground
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.ColorHighlight
import dev.snipme.highlights.model.SyntaxLanguage
import ru.kima.intelligentchat.presentation.common.components.conditional

private sealed class Block(val text: String) {
    class Normal(text: String) : Block(text)
    class Bold(text: String) : Block(text)
    class Italic(text: String) : Block(text)
}

@Composable
fun MarkdownText(text: String, modifier: Modifier = Modifier) {
    val mdBlocks = remember(text) { parseText(text) }
    Text(
        buildAnnotatedString {
            mdBlocks.forEach { block ->
                when (block) {
                    is Block.Bold -> withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        append(block.text)
                    }

                    is Block.Italic -> withStyle(
                        style = SpanStyle(
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(block.text)
                    }

                    is Block.Normal -> append(block.text)
                }
            }
        },
        modifier = modifier,
        style = MaterialTheme.typography.bodyLarge
    )
}

private fun parseText(text: String): List<Block> {
    var boldStart = -1
    var italicStart = -1
    var normalStart = 0

    val res = mutableListOf<Block>()

    text.forEachIndexed { index, c ->
        when (c) {
            '*' -> {
                if (boldStart < 0) {
                    boldStart = index

                    if (italicStart > 0) {
                        res.add(Block.Italic(text.slice(boldStart until index)))
                        italicStart = -1
                    } else {
                        res.add(Block.Normal(text.slice(normalStart until index)))
                    }
                } else {
                    res.add(Block.Bold(text.slice(boldStart + 1 until index)))
                    boldStart = -1
                    normalStart = index + 1
                }
            }

            '"' -> {
                if (italicStart < 0) {
                    italicStart = index

                    if (boldStart > 0) {
                        res.add(Block.Bold(text.slice(boldStart + 1 until index)))
                        boldStart = -1
                    } else {
                        res.add(Block.Normal(text.slice(normalStart until index)))
                    }
                } else {
                    res.add(Block.Italic(text.slice(italicStart..index)))
                    italicStart = -1
                    normalStart = index + 1
                }
            }

            else -> return@forEachIndexed
        }
    }

    if (res.isEmpty()) {
        res.add(Block.Normal(text))
    }

    return res
}

@Composable
fun CustomMarkdownHighlightedCode(
    code: String,
    language: String?,
    highlights: Highlights.Builder = Highlights.Builder(),
    style: TextStyle = LocalMarkdownTypography.current.code,
) {
    val backgroundCodeColor = LocalMarkdownColors.current.codeBackground
    val codeBackgroundCornerSize = LocalMarkdownDimens.current.codeBackgroundCornerSize
    val codeBlockPadding = LocalMarkdownPadding.current.codeBlock
    val syntaxLanguage = remember(language) { language?.let { SyntaxLanguage.getByName(it) } }

    val codeHighlights by remember(code) {
        derivedStateOf {
            highlights
                .code(code)
                .let { if (syntaxLanguage != null) it.language(syntaxLanguage) else it }
                .build()
        }
    }

    MarkdownCodeBackground(
        color = backgroundCodeColor,
        shape = RoundedCornerShape(codeBackgroundCornerSize),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        MarkdownBasicText(
            buildAnnotatedString {
                append(codeHighlights.getCode())
                if (highlights.language != SyntaxLanguage.DEFAULT) {
                    codeHighlights.getHighlights()
                        .filterIsInstance<ColorHighlight>()
                        .forEach {
                            addStyle(
                                SpanStyle(color = Color(it.rgb).copy(alpha = 1f)),
                                start = it.location.start,
                                end = it.location.end,
                            )
                        }
                }
                codeHighlights.getHighlights()
                    .filterIsInstance<BoldHighlight>()
                    .forEach {
                        addStyle(
                            SpanStyle(fontWeight = FontWeight.Bold),
                            start = it.location.start,
                            end = it.location.end,
                        )
                    }
            },
            color = LocalMarkdownColors.current.codeText,
            modifier = Modifier
                .conditional(highlights.language != SyntaxLanguage.DEFAULT) {
                    horizontalScroll(rememberScrollState())
                }
                .padding(codeBlockPadding),
            style = style
        )
    }
}
