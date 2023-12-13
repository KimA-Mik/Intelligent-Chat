package ru.kima.intelligentchat.di

import android.content.Context
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.data.image.dataSource.ImageStorage

fun data(context: Context) = module {
    single { DatabaseWrapper(context) }
    single { ImageStorage(context) }
    single {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}