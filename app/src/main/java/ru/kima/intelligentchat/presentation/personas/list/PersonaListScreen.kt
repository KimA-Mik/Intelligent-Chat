package ru.kima.intelligentchat.presentation.personas.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@Composable
fun PersonaListScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val scope = rememberCoroutineScope()

        Column {
            Text(text = "Personas list")
            Button(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Text(text = "Open")
            }
        }
    }
}