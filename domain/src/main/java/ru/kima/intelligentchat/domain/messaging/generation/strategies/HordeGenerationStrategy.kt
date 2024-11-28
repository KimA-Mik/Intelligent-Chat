package ru.kima.intelligentchat.domain.messaging.generation.strategies

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import ru.kima.intelligentchat.domain.common.ICResult
import ru.kima.intelligentchat.domain.common.valueOr
import ru.kima.intelligentchat.domain.horde.model.GenerationInput
import ru.kima.intelligentchat.domain.horde.model.HordeDefaults
import ru.kima.intelligentchat.domain.horde.model.HordeGenerationParams
import ru.kima.intelligentchat.domain.horde.repositoty.HordeRepository
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationRequest
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStatus
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStrategy
import ru.kima.intelligentchat.domain.messaging.generation.prompting.constructPrompt
import ru.kima.intelligentchat.domain.preferences.horde.useCase.GetHordePreferencesUseCase
import ru.kima.intelligentchat.domain.presets.kobold.useCase.GetKoboldPresetUseCase
import kotlin.random.Random

class HordeGenerationStrategy(
    private val hordeRepository: HordeRepository,
    private val getHordePreferences: GetHordePreferencesUseCase,
    private val getKoboldPreset: GetKoboldPresetUseCase
) : GenerationStrategy {
    private var cancelled = false

    override fun generation(request: GenerationRequest): Flow<GenerationStatus> = flow {
        cancelled = false
        emit(GenerationStatus.Pending)
        val hordeState = getHordePreferences().first()

        val apiKey = hordeState.apiToken.ifBlank { HordeDefaults.API_KEY }
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

        val hordeRequest = hordeRepository.requestGeneration(apiKey, generationInput).valueOr {
            emit(GenerationStatus.Error(it))
            return@flow
        }

        val id = hordeRequest.id
        emit(GenerationStatus.Started(id))
        var status = hordeRepository.getGenerationRequestStatus(id).valueOr {
            emit(GenerationStatus.Error(it))
            return@flow
        }

        while (!cancelled && !status.done && !status.faulted && status.isPossible) {
            if (status.waiting > 0) {
                emit(GenerationStatus.Pending)
            } else if (status.processing > 0) {
                emit(GenerationStatus.Generating)
            }

            delay(getDelayTime())
            status = hordeRepository.getGenerationRequestStatus(id).valueOr {
                emit(GenerationStatus.Error(it))
                return@flow
            }
        }

        if (status.done && status.generations.isNotEmpty()) {
            emit(GenerationStatus.Done(status.generations.first().text))
        } else if (cancelled) {
            emit(GenerationStatus.Aborted)
        }
    }

    private fun getDelayTime(): Long {
        return 1000L + (Random.nextFloat() * 1000L).toLong()
    }

    override suspend fun cancelGeneration(requestId: String): Boolean {
        cancelled = true
        return when (hordeRepository.cancelGenerationRequest(requestId)) {
            is ICResult.Error -> false
            is ICResult.Success -> true
        }
    }
}