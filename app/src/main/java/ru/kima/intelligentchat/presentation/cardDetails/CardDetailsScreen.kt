package ru.kima.intelligentchat.presentation.cardDetails

import android.Manifest
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.presentation.cardDetails.events.CardDetailUserEvent
import ru.kima.intelligentchat.presentation.cardDetails.events.UiEvent
import ru.kima.intelligentchat.presentation.common.image.ImagePicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    imagePicker: ImagePicker,
    viewModel: CardDetailsViewModel
) {
    BackHandler(onBack = {
        navController.popBackStack()
    })
    val state by viewModel.state.collectAsState()
    imagePicker.registerPicker { imageBytes ->
        viewModel.onEvent(CardDetailUserEvent.UpdateCardImage(imageBytes))
    }

    val scope = rememberCoroutineScope()

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
//                    imagePickerCheck.launch(actualPhotoPermission)
                    imagePicker.pickImage()
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

    val scrollBehavior =
        TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {},
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(CardDetailUserEvent.SaveCard) }) {
                        Icon(
                            imageVector = Icons.Filled.Save,
                            contentDescription = "Save card",
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        CardDetailContent(
            state,
            onEvent = viewModel::onEvent,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        )
    }
}
