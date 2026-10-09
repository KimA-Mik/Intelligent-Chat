package ru.kima.intelligentchat.presentation.common.markdown

import androidx.compose.runtime.Composable
import coil3.compose.rememberAsyncImagePainter
import com.mikepenz.markdown.model.ImageData
import com.mikepenz.markdown.model.ImageTransformer

class CoilMarkdownImageTransformer(
    private val showImages: Boolean
) : ImageTransformer {
    @Composable
    override fun transform(link: String): ImageData? {
        if (!showImages) return null

        return ImageData(
            painter = rememberAsyncImagePainter(model = link),
            contentDescription = null
        )
    }
}
