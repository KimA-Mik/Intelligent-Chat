package ru.kima.intelligentchat.presentation.common.markdown

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

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