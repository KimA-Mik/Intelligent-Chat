package ru.kima.intelligentchat.presentation.ui.theme.colorScheme

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme

@RequiresApi(Build.VERSION_CODES.S)
class MonetColorScheme(private val context: Context) : ICColorScheme() {
    override val darkScheme: ColorScheme
        get() = dynamicDarkColorScheme(context)
    override val lightScheme: ColorScheme
        get() = dynamicLightColorScheme(context)
}