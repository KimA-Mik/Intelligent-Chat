package ru.kima.intelligentchat

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.kima.intelligentchat.data.repository.CharacterCardRepositoryImpl
import ru.kima.intelligentchat.domain.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.useCase.characterCard.GetCardsUseCase
import ru.kima.intelligentchat.domain.useCase.characterCard.PutCardUseCase


class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val useCases = module {
            singleOf(::GetCardsUseCase)
            singleOf(::PutCardUseCase)
        }

        startKoin {
            androidLogger()
            androidContext(this@ChatApplication)
            modules(
                useCases,
                module {
                    single<CharacterCardRepository> { CharacterCardRepositoryImpl(get()) }
                }
            )
        }
    }
}