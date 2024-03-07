package ru.kima.intelligentchat.di

import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.data.image.dataSource.ImageStorage

fun data() = module {
    singleOf(::DatabaseWrapper)
    singleOf(::ImageStorage)
    single {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}