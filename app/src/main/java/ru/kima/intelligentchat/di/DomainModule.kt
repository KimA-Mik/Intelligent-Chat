package ru.kima.intelligentchat.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.kima.intelligentchat.data.card.repository.CharacterCardRepositoryImpl
import ru.kima.intelligentchat.data.persona.PersonaRepositoryImpl
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.card.useCase.AddCardFromPngUseCase
import ru.kima.intelligentchat.domain.card.useCase.DeleteCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardsListUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardsUseCase
import ru.kima.intelligentchat.domain.card.useCase.PutCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardAvatarUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardUseCase
import ru.kima.intelligentchat.domain.persona.repository.PersonaRepository
import ru.kima.intelligentchat.domain.persona.useCase.CreatePersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.DeletePersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.GetPersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.GetPersonasUseCase
import ru.kima.intelligentchat.domain.persona.useCase.LoadPersonaImageUseCase
import ru.kima.intelligentchat.domain.persona.useCase.SelectedPersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.SubscribeToPersonaUseCase
import ru.kima.intelligentchat.domain.persona.useCase.UpdatePersonaImageUseCase
import ru.kima.intelligentchat.domain.persona.useCase.UpdatePersonaUseCase

fun domain() = module {
    single<CharacterCardRepository> { CharacterCardRepositoryImpl(get(), get(), get()) }
    single<PersonaRepository> { PersonaRepositoryImpl(get(), get()) }

    factoryOf(::GetCardsUseCase)
    singleOf(::GetCardUseCase)
    singleOf(::PutCardUseCase)
    singleOf(::UpdateCardAvatarUseCase)
    singleOf(::AddCardFromPngUseCase)
    singleOf(::UpdateCardUseCase)
    singleOf(::DeleteCardUseCase)
    singleOf(::GetCardsListUseCase)


    singleOf(::CreatePersonaUseCase)
    singleOf(::SubscribeToPersonaUseCase)
    singleOf(::GetPersonaUseCase)
    singleOf(::GetPersonasUseCase)
    singleOf(::LoadPersonaImageUseCase)
    singleOf(::UpdatePersonaUseCase)
    singleOf(::UpdatePersonaImageUseCase)
    singleOf(::DeletePersonaUseCase)
    factoryOf(::SelectedPersonaUseCase)
}