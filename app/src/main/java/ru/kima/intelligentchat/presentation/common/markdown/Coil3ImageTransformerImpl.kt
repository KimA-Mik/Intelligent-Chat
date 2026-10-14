package ru.kima.intelligentchat.presentation.common.markdown

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.mikepenz.markdown.model.ImageData
import com.mikepenz.markdown.model.ImageTransformer
import com.mikepenz.markdown.model.PlaceholderConfig

class Coil3ImageTransformerImpl(
    private val showImage: Boolean
) : ImageTransformer {
    override fun placeholderConfig(
        density: Density,
        containerSize: Size,
        intrinsicImageSize: Size
    ): PlaceholderConfig = super.placeholderConfig(
        density = density,
        containerSize = containerSize,
        intrinsicImageSize = intrinsicImageSize
    ).copy(verticalAlign = PlaceholderVerticalAlign.AboveBaseline)

    @Composable
    override fun transform(link: String): ImageData {
        return rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(if (showImage) link else null)
                .size(coil3.size.Size.ORIGINAL)
                .build()
        ).let { painter ->
            ImageData(
                painter = painter,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
            )
        }
    }

    @Composable
    override fun intrinsicSize(painter: Painter): Size {
        if (!showImage) return Size.Unspecified
        var size by remember(painter) { mutableStateOf(painter.intrinsicSize) }
        if (painter is AsyncImagePainter) {
            val painterState by painter.state.collectAsState()
            val intrinsicSize = painterState.painter?.intrinsicSize
            intrinsicSize?.also { size = it }
        }
        return size
    }
}