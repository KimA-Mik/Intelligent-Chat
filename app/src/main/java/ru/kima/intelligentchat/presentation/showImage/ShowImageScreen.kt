package ru.kima.intelligentchat.presentation.showImage

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import ru.kima.intelligentchat.common.photoNameToFile


@Composable
fun ShowImageScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: ShowImageViewModel
) {
    val photoName by viewModel.photoName.collectAsState()
    LaunchedEffect(true) {
        viewModel.uiEvents.collect { event ->
            when (event) {
                ShowImageViewModel.UiEvent.Close -> navController.popBackStack()
                is ShowImageViewModel.UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { contentPadding ->
        ShowImageContent(
            photoName = photoName,
            Modifier.padding(contentPadding),
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun ShowImageContent(
    photoName: String?,
    modifier: Modifier,
    onEvent: (ShowImageViewModel.UserEvent) -> Unit
) {
    val config = LocalConfiguration.current

    val screenHeight = config.screenHeightDp.toFloat()
    val screenWidth = config.screenWidthDp.toFloat()

    var imageHeight by remember { mutableFloatStateOf(1f) }

    Box(modifier = modifier) {
        var scale by remember { mutableFloatStateOf(1f) }
        var offsetX by remember { mutableFloatStateOf(0f) }
        var offsetY by remember { mutableFloatStateOf(0f) }

        val animateX by animateFloatAsState(
            targetValue = offsetX, label = "X",
            animationSpec = SpringSpec(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessHigh
            )
        )
        val animateY by animateFloatAsState(
            targetValue = offsetY, label = "Y",
            animationSpec = SpringSpec(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessHigh
            )
        )
        val animateScale by animateFloatAsState(
            targetValue = scale, label = "scale",
            animationSpec = SpringSpec(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )

        //TODO: Fix offset
        val state = rememberTransformableState { zoomChange, offsetChange, _ ->
            scale *= zoomChange
            if (scale < 1) {
                scale = 1f
            } else if (scale > 4) {
                scale = 4f
            }

            offsetX += offsetChange.x * scale * 1.5f
            if (offsetX > screenWidth) {
                offsetX = screenWidth
            } else if (offsetX < -screenWidth) {
                offsetX = -screenWidth
            }


            offsetY += offsetChange.y * scale * 1.5f
//            if (offsetY > screenHeight) {
//                offsetY = screenHeight
//            } else if (offsetY < -screenHeight) {
//                offsetY = -screenHeight
//            }
            println(offsetY)
            if (offsetY > (scale - 1) * imageHeight) {
                offsetY = (scale - 1) * imageHeight
            } else if (offsetY < -(scale - 1) * imageHeight) {
                offsetY = -(scale - 1) * imageHeight
            }
        }

        val context = LocalContext.current
        val file = remember(photoName) {
            context.photoNameToFile(photoName)
        }

        AsyncImage(
            model = file, contentDescription = "This is an image",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = animateScale,
                    scaleY = animateScale,
                    translationX = animateX,
                    translationY = animateY
                )
                .transformable(state),
//                .offset(animateX, animateY)
            onSuccess = {
                imageHeight = it.result.image.height.toFloat()
            }
        )

        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            onClick = { onEvent(ShowImageViewModel.UserEvent.OnCloseClicked) }) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
        }
    }
}
