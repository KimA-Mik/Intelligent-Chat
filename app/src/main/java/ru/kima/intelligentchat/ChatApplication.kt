package ru.kima.intelligentchat

import android.app.Application
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.kima.intelligentchat.data.local.dataSource.ImageStorage
import ru.kima.intelligentchat.data.repository.CharacterCardRepositoryImpl
import ru.kima.intelligentchat.domain.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.useCase.characterCard.AddCardFromPngUseCase
import ru.kima.intelligentchat.domain.useCase.characterCard.GetCardUseCase
import ru.kima.intelligentchat.domain.useCase.characterCard.GetCardsUseCase
import ru.kima.intelligentchat.domain.useCase.characterCard.PutCardUseCase
import ru.kima.intelligentchat.domain.useCase.characterCard.UpdateCardAvatarUseCase
import ru.kima.intelligentchat.presentation.cardDetails.CardDetailsViewModel
import ru.kima.intelligentchat.presentation.charactersList.CharactersListViewModel
import ru.kima.intelligentchat.presentation.common.image.ImagePicker


class ChatApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        val useCases = module {
            singleOf(::GetCardsUseCase)
            singleOf(::GetCardUseCase)
            singleOf(::PutCardUseCase)
            singleOf(::UpdateCardAvatarUseCase)
            singleOf(::AddCardFromPngUseCase)
        }

        val viewModels = module {
            viewModelOf(::CharactersListViewModel)
            viewModelOf(::CardDetailsViewModel)
        }

        startKoin {
            androidLogger()
            modules(
                useCases,
                viewModels,
                module {
                    single<CharacterCardRepository> {
                        CharacterCardRepositoryImpl(
                            this@ChatApplication,
                            get(),
                            get()
                        )
                    }
                    single { ImageStorage(this@ChatApplication) }
                    single { ImagePicker(this@ChatApplication) }
                    single {
                        Json {
                            ignoreUnknownKeys = true
                            encodeDefaults = true
                        }
                    }
                }
            )
        }
    }
}