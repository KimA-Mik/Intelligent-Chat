package ru.kima.intelligentchat.di

import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.kima.intelligentchat.data.common.DatabaseWrapper
import ru.kima.intelligentchat.data.image.dataSource.InternalImageStorage
import ru.kima.intelligentchat.domain.images.ImageStorage

fun data() = module {
    singleOf(::DatabaseWrapper)
    singleOf(::InternalImageStorage) bind ImageStorage::class
    single {
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        }
    }
}