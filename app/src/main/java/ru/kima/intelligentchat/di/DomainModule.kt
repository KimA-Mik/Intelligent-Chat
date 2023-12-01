package ru.kima.intelligentchat.di

import android.content.Context
import org.koin.dsl.module
import ru.kima.intelligentchat.data.card.repository.CharacterCardRepositoryImpl
import ru.kima.intelligentchat.data.image.repository.ImageRepositoryImpl
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.card.useCase.AddCardFromPngUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardsUseCase
import ru.kima.intelligentchat.domain.card.useCase.PutCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardAvatarUseCase
import ru.kima.intelligentchat.domain.image.repository.ImageRepository

fun domain(context: Context) = module {
    single<CharacterCardRepository> { CharacterCardRepositoryImpl(context, get(), get()) }
    single<ImageRepository> { ImageRepositoryImpl(get()) }

    single { GetCardUseCase(get()) }
    single { GetCardsUseCase(get()) }
    single { PutCardUseCase(get()) }
    single { UpdateCardAvatarUseCase(get(), get()) }
    single { AddCardFromPngUseCase(get(), get()) }

}