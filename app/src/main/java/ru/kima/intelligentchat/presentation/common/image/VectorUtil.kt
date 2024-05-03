package ru.kima.intelligentchat.presentation.common.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.RenderVectorGroup
import androidx.compose.ui.graphics.vector.VectorConfig
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter

@Composable
fun rememberVectorPainter(
    image: ImageVector,
    tint: Color,
    tintBlendMode: BlendMode = BlendMode.SrcIn,
    configs: Map<String, VectorConfig> = emptyMap()
): VectorPainter {
    return rememberVectorPainter(
        defaultWidth = image.defaultHeight,
        defaultHeight = image.defaultHeight,
        viewportWidth = image.viewportWidth,
        viewportHeight = image.viewportHeight,
        name = image.root.name,
        tintColor = tint,
        tintBlendMode = tintBlendMode,
        autoMirror = image.autoMirror
    ) { _, _ ->
        RenderVectorGroup(group = image.root, configs = configs)
    }
}