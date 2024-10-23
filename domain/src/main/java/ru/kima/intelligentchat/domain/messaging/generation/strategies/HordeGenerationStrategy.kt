package ru.kima.intelligentchat.domain.messaging.generation.strategies

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import ru.kima.intelligentchat.core.common.ICResult
import ru.kima.intelligentchat.domain.horde.model.GenerationInput
import ru.kima.intelligentchat.domain.horde.model.HordeGenerationParams
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationRequest
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStatus
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStrategy
import ru.kima.intelligentchat.domain.messaging.generation.prompting.constructPrompt
import ru.kima.intelligentchat.domain.preferences.horde.useCase.GetHordePreferencesUseCase
import ru.kima.intelligentchat.domain.presets.kobold.useCase.GetKoboldPresetUseCase

class HordeGenerationStrategy(
    private val hordeRepository: HordeRepository,
    private val getHordePreferences: GetHordePreferencesUseCase,
    private val getKoboldPreset: GetKoboldPresetUseCase
) : GenerationStrategy {
    override fun generate(request: GenerationRequest): Flow<GenerationStatus> = flow {
        emit(GenerationStatus.None)
        val hordeState = getHordePreferences().last()

        val apiKey = hordeState.apiToken
        val preset = getKoboldPreset(hordeState.selectedPreset) ?: return@flow
        val params = HordeGenerationParams(
            maxContextLength = request.maxContextLength,
            maxLength = request.maxResponseLength,
            minP = preset.minP,
            mirostat = preset.mirostat,
            mirostatEta = preset.mirostatEta,
            mirostatTau = preset.mirostatTau,
            repPen = preset.repetitionPenalty,
            repetitionPenaltyRange = preset.repetitionPenaltyRange,
            repetitionPenaltySlope = preset.repetitionPenaltySlope,
            samplerOrder = preset.samplerOrder,
            stopSequence = request.stopSequence,
            temperature = preset.temperature,
            tailFreeSampling = preset.tailFreeSampling,
            topA = preset.topA,
            topK = preset.topK,
            topP = preset.topP,
            typical = preset.typical,
            useDefaultBadWordsIds = true,
            useWorldInfo = false,
        )

        val generationInput = GenerationInput(
            models = hordeState.selectedModels,
            params = params,
            prompt = request.constructPrompt(),
            trustedWorkers = hordeState.trustedWorkers
        )

        when (hordeRepository.requestGeneration(apiKey, generationInput)) {
            is ICResult.Error -> TODO()
            is ICResult.Success -> TODO()
        }
    }

    override suspend fun cancelGeneration(requestId: String) {
        TODO("Not yet implemented")
    }
}