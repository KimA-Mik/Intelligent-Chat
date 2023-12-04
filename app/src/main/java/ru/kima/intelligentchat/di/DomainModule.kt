package ru.kima.intelligentchat.di

import android.content.Context
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.kima.intelligentchat.data.card.repository.CharacterCardRepositoryImpl
import ru.kima.intelligentchat.data.image.repository.ImageRepositoryImpl
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.card.useCase.AddCardFromPngUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardsUseCase
import ru.kima.intelligentchat.domain.card.useCase.PutCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardAvatarUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardUseCase
import ru.kima.intelligentchat.domain.image.repository.ImageRepository
import ru.kima.intelligentchat.domain.image.useCase.GetImageUseCase
import ru.kima.intelligentchat.domain.image.useCase.SaveImageUseCase

fun domain(context: Context) = module {
    single<CharacterCardRepository> { CharacterCardRepositoryImpl(context, get(), get()) }
    single<ImageRepository> { ImageRepositoryImpl(get()) }

    singleOf(::GetCardUseCase)
    singleOf(::GetCardsUseCase)
    singleOf(::PutCardUseCase)
    singleOf(::UpdateCardAvatarUseCase)
    singleOf(::AddCardFromPngUseCase)
    singleOf(::UpdateCardUseCase)

    singleOf(::GetImageUseCase)
    singleOf(::SaveImageUseCase)

}