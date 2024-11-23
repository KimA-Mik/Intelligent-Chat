package ru.kima.intelligentchat.presentation.common.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.kima.intelligentchat.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    // Title
    titleContent: @Composable () -> Unit,

    modifier: Modifier = Modifier,
    backgroundColor: Color? = null,
    // Up button
    navigateUp: (() -> Unit)? = null,
    navigationIcon: ImageVector? = null,
    // Menu
    actions: @Composable RowScope.() -> Unit = {},
    // Action mode
    isActionMode: Boolean = false,
    onCancelActionMode: () -> Unit = {},

    colors: TopAppBarColors? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    Column(
        modifier = modifier,
    ) {
        TopAppBar(
            navigationIcon = {
                if (isActionMode) {
                    IconButton(onClick = onCancelActionMode) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(R.string.action_cancel),
                        )
                    }
                } else {
                    navigateUp?.let {
                        IconButton(onClick = it) {
                            UpIcon(navigationIcon = navigationIcon)
                        }
                    }
                }
            },
            title = titleContent,
            actions = actions,
            colors = colors ?: TopAppBarDefaults.topAppBarColors(
                containerColor = backgroundColor
                    ?: MaterialTheme.colorScheme.surfaceColorAtElevation(
                        elevation = if (isActionMode) 3.dp else 0.dp,
                    ),
            ),
            scrollBehavior = scrollBehavior,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchToolbar(
    searchQuery: String?,
    onChangeSearchQuery: (String?) -> Unit,
    modifier: Modifier = Modifier,
    titleContent: @Composable () -> Unit = {},
    navigateUp: (() -> Unit)? = null,
    navigationIcon: ImageVector? = null,
    searchEnabled: Boolean = true,
    placeholderText: String? = null,
    onSearch: (String) -> Unit = {},
    onClickCloseSearch: () -> Unit = { onChangeSearchQuery(null) },
    closeSearchIcon: ImageVector? = Icons.AutoMirrored.Filled.ArrowBack,
    actions: @Composable RowScope.() -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val focusRequester = remember { FocusRequester() }

    AppBar(
        modifier = modifier,
        titleContent = {
            if (searchQuery == null) return@AppBar titleContent()

            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current

            val searchAndClearFocus: () -> Unit = f@{
                if (searchQuery.isBlank()) return@f
                onSearch(searchQuery)
                focusManager.clearFocus()
                keyboardController?.hide()
            }

            BasicTextField(
                value = searchQuery,
                onValueChange = onChangeSearchQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .runOnEnterKeyPressed(action = searchAndClearFocus)
                    .showSoftKeyboard(remember { searchQuery.isEmpty() })
                    .clearFocusOnSoftKeyboardHide(),
                textStyle = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { searchAndClearFocus() }),
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                decorationBox = { innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = searchQuery,
                        innerTextField = innerTextField,
                        enabled = true,
                        singleLine = true,
                        visualTransformation = visualTransformation,
                        interactionSource = interactionSource,
                        placeholder = {
                            Text(
                                modifier = Modifier.alpha(.78f),
                                text = (placeholderText
                                    ?: stringResource(R.string.action_search_hint)),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                ),
                            )
                        },
                        container = {},
                    )
                },
            )
        },
        navigateUp = if (searchQuery == null) navigateUp else onClickCloseSearch,
        navigationIcon = if (searchQuery == null) navigationIcon else closeSearchIcon,
        actions = {
            key("search") {
                val onClick = { onChangeSearchQuery("") }

                if (!searchEnabled) {
                    // Don't show search action
                } else if (searchQuery == null) {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text(stringResource(R.string.action_search))
                            }
                        },
                        state = rememberTooltipState(),
                    ) {
                        IconButton(
                            onClick = onClick,
                        ) {
                            Icon(
                                Icons.Outlined.Search,
                                contentDescription = stringResource(R.string.action_search),
                            )
                        }
                    }
                } else if (searchQuery.isNotEmpty()) {
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text(stringResource(R.string.action_reset))
                            }
                        },
                        state = rememberTooltipState(),
                    ) {
                        IconButton(
                            onClick = {
                                onClick()
                                focusRequester.requestFocus()
                            },
                        ) {
                            Icon(
                                Icons.Outlined.Close,
                                contentDescription = stringResource(R.string.action_reset),
                            )
                        }
                    }
                }
            }

            key("actions") { actions() }
        },
        isActionMode = false,
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun UpIcon(
    modifier: Modifier = Modifier,
    navigationIcon: ImageVector? = null,
) {
    val icon = navigationIcon
        ?: Icons.AutoMirrored.Outlined.ArrowBack
    Icon(
        imageVector = icon,
        contentDescription = stringResource(R.string.action_bar_up_description),
        modifier = modifier,
    )
}
