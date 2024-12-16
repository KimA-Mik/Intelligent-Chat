package ru.kima.intelligentchat.presentation.settings.screen.extended.instructModeTemplate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.presentation.common.components.AppBar
import ru.kima.intelligentchat.presentation.ui.LocalNavController
import ru.kima.intelligentchat.presentation.ui.theme.IntelligentChatTheme

@Composable
fun InstructModeTemplateRoot() {
    InstructModeTemplateScreen(Modifier.fillMaxSize())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructModeTemplateScreen(modifier: Modifier = Modifier) {
    val nacController = LocalNavController.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = modifier,
        topBar = {
            AppBar(
                titleContent = { Text(stringResource(R.string.instruct_mode_setting_title)) },
                navigateUp = { nacController.popBackStack() },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        InstructModeTemplateContent(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        )
    }
}

@Composable
fun InstructModeTemplateContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.instruct_mode_setting_title))
    }
}

@Preview
@Composable
private fun InstructModeTemplateScreenPreview() {
    IntelligentChatTheme {
        Surface {
            InstructModeTemplateScreen()
        }
    }
}
