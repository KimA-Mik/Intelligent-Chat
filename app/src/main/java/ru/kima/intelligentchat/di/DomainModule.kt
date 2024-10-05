package ru.kima.intelligentchat.di

import android.content.Context
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.get
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.data.card.repository.CharacterCardRepositoryImpl
import ru.kima.intelligentchat.data.chat.repository.ChatRepositoryImpl
import ru.kima.intelligentchat.data.chat.repository.MessageRepositoryImpl
import ru.kima.intelligentchat.data.chat.repository.SwipeRepositoryImpl
import ru.kima.intelligentchat.data.kobold.horde.HordeRepositoryImpl
import ru.kima.intelligentchat.data.kobold.preset.repository.KoboldPresetRepositoryImpl
import ru.kima.intelligentchat.data.persona.PersonaRepositoryImpl
import ru.kima.intelligentchat.domain.card.repository.CharacterCardRepository
import ru.kima.intelligentchat.domain.card.useCase.AddCardFromPngUseCase
import ru.kima.intelligentchat.domain.card.useCase.CreateAlternateGreetingUseCase
import ru.kima.intelligentchat.domain.card.useCase.DeleteAlternateGreetingUseCase
import ru.kima.intelligentchat.domain.card.useCase.DeleteCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardsListUseCase
import ru.kima.intelligentchat.domain.card.useCase.GetCardsUseCase
import ru.kima.intelligentchat.domain.card.useCase.PutCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateAlternateGreetingUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardAvatarUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardUseCase
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository
import ru.kima.intelligentchat.domain.chat.repository.SwipeRepository
import ru.kima.intelligentchat.domain.chat.useCase.CreateAndSelectChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToCardChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToChatMessagesWithSwipesUseCase
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToFullChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.SwipeFirstMessageUseCase
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository
import ru.kima.intelligentchat.domain.horde.useCase.GetKudosUseCase
import ru.kima.intelligentchat.domain.horde.useCase.LoadHordeModelsUseCase
import ru.kima.intelligentchat.domain.horde.useCase.SaveApiKeyUseCase
import ru.kima.intelligentchat.domain.horde.useCase.SelectActiveHordePresetUseCase
import ru.kima.intelligentchat.domain.messaging.repositoty.MessagingRepository
import ru.kima.intelligentchat.domain.messaging.useCase.LoadMessagingDataUseCase
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
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.SetSelectedPersonaIdUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.UpdateSelectedApiUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.GetHordePreferencesUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.SelectHordeModelsUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateActualGenerationDetailsUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateContextToWorkerUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateHordeUserDataUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateResponseToWorkerUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateTrustedWorkersUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateUserGenerationDetailsUseCase
import ru.kima.intelligentchat.domain.presets.kobold.repository.KoboldPresetRepository
import ru.kima.intelligentchat.domain.presets.kobold.useCase.GetKoboldPresetUseCase
import ru.kima.intelligentchat.domain.presets.kobold.useCase.SubscribeToKoboldPresetsUseCase
import ru.kima.intelligentchat.domain.presets.kobold.useCase.UpdateKoboldPresetUseCase
import ru.kima.intelligentchat.domain.tokenizer.LlamaTokenizer
import ru.kima.intelligentchat.domain.tokenizer.useCase.TokenizeTextUseCase
import ru.kima.intelligentchat.presentation.android.implementation.messaging.repositoty.MessagingRepositoryImpl

@OptIn(ExperimentalSerializationApi::class)
fun domain() = module {
    singleOf(::CharacterCardRepositoryImpl) bind CharacterCardRepository::class
    singleOf(::PersonaRepositoryImpl) bind PersonaRepository::class
    singleOf(::HordeRepositoryImpl) bind HordeRepository::class
    singleOf(::KoboldPresetRepositoryImpl) bind KoboldPresetRepository::class
    singleOf(::ChatRepositoryImpl) bind ChatRepository::class
    singleOf(::MessageRepositoryImpl) bind MessageRepository::class
    singleOf(::SwipeRepositoryImpl) bind SwipeRepository::class
    singleOf(::MessagingRepositoryImpl) bind MessagingRepository::class

    singleOf(::GetPreferencesUseCase)
    singleOf(::SetSelectedPersonaIdUseCase)
    singleOf(::UpdateSelectedApiUseCase)

    singleOf(::GetHordePreferencesUseCase)
    singleOf(::UpdateContextToWorkerUseCase)
    singleOf(::UpdateHordeUserDataUseCase)
    singleOf(::UpdateResponseToWorkerUseCase)
    singleOf(::UpdateTrustedWorkersUseCase)
    singleOf(::SelectHordeModelsUseCase)
    singleOf(::UpdateUserGenerationDetailsUseCase)
    singleOf(::UpdateActualGenerationDetailsUseCase)

    factoryOf(::GetCardsUseCase)
    factoryOf(::GetCardsListUseCase)
    singleOf(::GetCardUseCase)
    singleOf(::PutCardUseCase)
    singleOf(::UpdateCardAvatarUseCase)
    singleOf(::AddCardFromPngUseCase)
    singleOf(::UpdateCardUseCase)
    singleOf(::DeleteCardUseCase)

    singleOf(::CreateAlternateGreetingUseCase)
    singleOf(::DeleteAlternateGreetingUseCase)
    singleOf(::UpdateAlternateGreetingUseCase)

    singleOf(::CreatePersonaUseCase)
    singleOf(::SubscribeToPersonaUseCase)
    singleOf(::GetPersonaUseCase)
    singleOf(::GetPersonasUseCase)
    singleOf(::LoadPersonaImageUseCase)
    singleOf(::UpdatePersonaUseCase)
    singleOf(::UpdatePersonaImageUseCase)
    singleOf(::DeletePersonaUseCase)
    factoryOf(::SelectedPersonaUseCase)

    factoryOf(::TokenizeTextUseCase)

    singleOf(::GetKudosUseCase)
    singleOf(::LoadHordeModelsUseCase)
    singleOf(::SaveApiKeyUseCase)
    singleOf(::SelectActiveHordePresetUseCase)

    singleOf(::GetKoboldPresetUseCase)
    singleOf(::SubscribeToKoboldPresetsUseCase)
    singleOf(::UpdateKoboldPresetUseCase)

    singleOf(::CreateAndSelectChatUseCase)
    singleOf(::SubscribeToCardChatUseCase)
    singleOf(::SubscribeToChatMessagesWithSwipesUseCase)
    singleOf(::SubscribeToFullChatUseCase)

    singleOf(::SwipeFirstMessageUseCase)

    singleOf(::LoadMessagingDataUseCase)

    single {
        val context: Context = get(Context::class.java)
        val vocabStream = context.resources.openRawResource(R.raw.llama_vocabulary)
        val vocabulary: List<String> = Json.decodeFromStream(vocabStream)
        vocabStream.close()

        val mergesStream = context.resources.openRawResource(R.raw.llama_merges_list)
        val merges = mutableMapOf<String, Int>()
        Json.decodeFromStream<List<String>>(mergesStream)
            .forEachIndexed { index, merge ->
                merges[merge] = index * 2 + 1
            }
        LlamaTokenizer(vocabulary, merges)
    }
}