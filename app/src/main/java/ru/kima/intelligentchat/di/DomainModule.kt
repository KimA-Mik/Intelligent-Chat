package ru.kima.intelligentchat.di

import android.content.Context
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.get
import ru.kima.intelligentchat.R
import ru.kima.intelligentchat.data.card.repository.CharacterCardRepositoryImpl
import ru.kima.intelligentchat.data.chat.advancedFormatting.contextTemplate.ContextTemplateRepositoryImpl
import ru.kima.intelligentchat.data.chat.advancedFormatting.instructMode.InstructModeTemplateRepositoryImpl
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
import ru.kima.intelligentchat.domain.card.useCase.RestoreCardUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateAlternateGreetingUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardAvatarUseCase
import ru.kima.intelligentchat.domain.card.useCase.UpdateCardUseCase
import ru.kima.intelligentchat.domain.chat.repository.ChatRepository
import ru.kima.intelligentchat.domain.chat.repository.MessageRepository
import ru.kima.intelligentchat.domain.chat.repository.SwipeRepository
import ru.kima.intelligentchat.domain.chat.useCase.CreateAndSelectChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.CreateChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.DeleteChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.InitializeChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.RenameChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.SelectChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToCardChatsUseCase
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToChatMessagesWithSwipesUseCase
import ru.kima.intelligentchat.domain.chat.useCase.SubscribeToFullChatUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.BranchChatFromMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.CreateMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.DeleteCurrentSwipeUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.DeleteMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.EditMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.MoveMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.RestoreMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.RestoreSwipeUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.SwipeFirstMessageUseCase
import ru.kima.intelligentchat.domain.chat.useCase.inChat.SwipeMessageUseCase
import ru.kima.intelligentchat.domain.common.useCase.CleanUpUseCase
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository
import ru.kima.intelligentchat.domain.horde.useCase.GetKudosUseCase
import ru.kima.intelligentchat.domain.horde.useCase.LoadHordeModelsUseCase
import ru.kima.intelligentchat.domain.horde.useCase.SaveApiKeyUseCase
import ru.kima.intelligentchat.domain.horde.useCase.SelectActiveHordePresetUseCase
import ru.kima.intelligentchat.domain.images.useCase.GetFreeImageNameUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.contextTemplate.ContextTemplateRepository
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.InstructModeTemplateRepository
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.useCase.CreateInstructModeTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.useCase.DeleteInstructModeTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.useCase.GetSelectedInstructTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.useCase.SelectInstructTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.useCase.SubscribeToInstructModeTemplatesUseCase
import ru.kima.intelligentchat.domain.messaging.advancedFormatting.instructMode.useCase.UpdateInstructModeTemplateUseCase
import ru.kima.intelligentchat.domain.messaging.generation.savingResult.DefaultSavingStrategy
import ru.kima.intelligentchat.domain.messaging.generation.savingResult.SwipeSavingStrategy
import ru.kima.intelligentchat.domain.messaging.generation.strategies.HordeGenerationStrategy
import ru.kima.intelligentchat.domain.messaging.generation.strategies.KoboldAiGenerationStrategy
import ru.kima.intelligentchat.domain.messaging.repositoty.MessagingRepository
import ru.kima.intelligentchat.domain.messaging.useCase.CancelMessageUseCase
import ru.kima.intelligentchat.domain.messaging.useCase.LoadMessagingConfigUseCase
import ru.kima.intelligentchat.domain.messaging.useCase.LoadMessagingDataUseCase
import ru.kima.intelligentchat.domain.messaging.useCase.SendMessageUseCase
import ru.kima.intelligentchat.domain.messaging.useCase.SubscribeToMessagingStatus
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
import ru.kima.intelligentchat.domain.preferences.advancedFormatting.AdvancedFormattingRepository
import ru.kima.intelligentchat.domain.preferences.app.AppPreferencesRepository
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.SetSelectedPersonaIdUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.UpdateSelectedApiUseCase
import ru.kima.intelligentchat.domain.preferences.chatSettings.ChatSettingsRepository
import ru.kima.intelligentchat.domain.preferences.horde.HordeStateRepository
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
import ru.kima.intelligentchat.presentation.android.preferences.advancedFormatting.AdvancedFormattingRepositoryImpl
import ru.kima.intelligentchat.presentation.android.preferences.appPreferences.AppPreferencesRepositoryImpl
import ru.kima.intelligentchat.presentation.android.preferences.chatSettings.ChatSettingsRepositoryImpl
import ru.kima.intelligentchat.presentation.android.preferences.hordeState.HordeStateRepositoryImpl

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
    singleOf(::InstructModeTemplateRepositoryImpl) bind InstructModeTemplateRepository::class
    singleOf(::ContextTemplateRepositoryImpl) bind ContextTemplateRepository::class

    singleOf(::AppPreferencesRepositoryImpl) bind AppPreferencesRepository::class
    singleOf(::ChatSettingsRepositoryImpl) {
        //Avoid switches flickering in settings screen
        createdAtStart()
    } bind ChatSettingsRepository::class
    singleOf(::HordeStateRepositoryImpl) bind HordeStateRepository::class
    singleOf(::AdvancedFormattingRepositoryImpl) bind AdvancedFormattingRepository::class

    factoryOf(::CleanUpUseCase)

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
    singleOf(::RestoreCardUseCase)
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

    singleOf(::GetFreeImageNameUseCase)

    singleOf(::GetKoboldPresetUseCase)
    singleOf(::SubscribeToKoboldPresetsUseCase)
    singleOf(::UpdateKoboldPresetUseCase)

    singleOf(::CreateAndSelectChatUseCase)
    singleOf(::CreateChatUseCase)
    singleOf(::DeleteChatUseCase)
    singleOf(::InitializeChatUseCase)
    singleOf(::RenameChatUseCase)
    singleOf(::SelectChatUseCase)
    singleOf(::SubscribeToCardChatsUseCase)
    singleOf(::SubscribeToChatMessagesWithSwipesUseCase)
    singleOf(::SubscribeToFullChatUseCase)

    singleOf(::BranchChatFromMessageUseCase)
    singleOf(::CreateMessageUseCase)
    singleOf(::DeleteCurrentSwipeUseCase)
    singleOf(::DeleteMessageUseCase)
    singleOf(::EditMessageUseCase)
    singleOf(::MoveMessageUseCase)
    singleOf(::RestoreMessageUseCase)
    singleOf(::RestoreSwipeUseCase)
    singleOf(::SwipeFirstMessageUseCase)
    singleOf(::SwipeMessageUseCase)

    singleOf(::CancelMessageUseCase)
    singleOf(::LoadMessagingConfigUseCase)
    singleOf(::LoadMessagingDataUseCase)
    singleOf(::SendMessageUseCase)
    singleOf(::SubscribeToMessagingStatus)

    singleOf(::CreateInstructModeTemplateUseCase)
    singleOf(::DeleteInstructModeTemplateUseCase)
    singleOf(::GetSelectedInstructTemplateUseCase)
    singleOf(::SelectInstructTemplateUseCase)
    singleOf(::SubscribeToInstructModeTemplatesUseCase)
    singleOf(::UpdateInstructModeTemplateUseCase)

    singleOf(::HordeGenerationStrategy)
    singleOf(::KoboldAiGenerationStrategy)

    factoryOf(::DefaultSavingStrategy)
    factoryOf(::SwipeSavingStrategy)

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