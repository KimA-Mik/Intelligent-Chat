package ru.kima.intelligentchat.presentation.cardDetails

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.kima.intelligentchat.presentation.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.cardDetails.events.UiEvent
import ru.kima.intelligentchat.presentation.common.image.ImagePicker

@Composable
fun CardDetailsScreen(
    navController: NavController,
    imagePicker: ImagePicker = koinInject(),
    viewModel: CardDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    imagePicker.registerPicker { imageBytes ->
        viewModel.onEvent(CardDetailUserEvent.UpdateCardImage(imageBytes))
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val actualPhotoPermission = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }


    val askForPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permissionGranted ->
        if (permissionGranted) {
            imagePicker.pickImage()
        }
    }
    val imagePickerCheck = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            imagePicker.pickImage()
        } else {
            askForPermission.launch(actualPhotoPermission)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvents.collect { uiEvent ->
            when (uiEvent) {
                UiEvent.SelectImage -> {
//                    imagePicker.launch("image/*")
                    imagePickerCheck.launch(actualPhotoPermission)
                }

                is UiEvent.SnackbarMessage -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            uiEvent.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        CardDetailContent(state, Modifier.padding(contentPadding)) { event ->
            viewModel.onEvent(event)
        }
    }
}