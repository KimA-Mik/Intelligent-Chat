package ru.kima.intelligentchat.presentation.common.components

import androidx.compose.ui.platform.UriHandler

class SimpleUriHandler(
    private val handle: (String) -> Unit
) : UriHandler {
    override fun openUri(uri: String) = handle(uri)
}
