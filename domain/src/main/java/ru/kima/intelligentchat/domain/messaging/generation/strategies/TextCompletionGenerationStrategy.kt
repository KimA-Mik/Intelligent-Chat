package ru.kima.intelligentchat.domain.messaging.generation.strategies

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.selects.select
import ru.kima.intelligentchat.domain.common.errors.GenerationError
import ru.kima.intelligentchat.domain.koboldcpp.KoboldCppRepository
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationRequest
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStatus
import ru.kima.intelligentchat.domain.messaging.generation.model.GenerationStrategy
import ru.kima.intelligentchat.domain.messaging.generation.model.KoboldCppGenerationInput
import ru.kima.intelligentchat.domain.messaging.generation.prompting.TemplateResolver
import ru.kima.intelligentchat.domain.messaging.generation.prompting.constructPrompt
import ru.kima.intelligentchat.domain.preferences.textCompletion.useCase.GetTextCompletionPreferencesUseCase
import ru.kima.intelligentchat.domain.presets.textCompletion.useCase.GetTextCompletionPresetUseCase
import java.util.UUID
import java.util.concurrent.atomic.AtomicReference

class TextCompletionGenerationStrategy(
    private val templateResolver: TemplateResolver,
    private val repository: KoboldCppRepository,
    private val getPreferences: GetTextCompletionPreferencesUseCase,
    private val getPreset: GetTextCompletionPresetUseCase
) : GenerationStrategy {
    private data class ActiveGeneration(
        val id: String,
        val cancellation: CompletableDeferred<Unit>
    )

    private val activeGeneration = AtomicReference<ActiveGeneration?>(null)

    override fun generation(request: GenerationRequest): Flow<GenerationStatus> = flow {
        emit(GenerationStatus.Pending)
        val preferences = getPreferences().first()
        val preset = getPreset(preferences.selectedPreset)
        if (preset == null) {
            emit(GenerationStatus.Error(GenerationError.Failure("No Text Completion preset is selected.")))
            return@flow
        }
        if (preferences.baseUrl.isBlank()) {
            emit(GenerationStatus.Error(GenerationError.Failure("Set the KoboldCpp base URL in Connection settings.")))
            return@flow
        }

        val input = KoboldCppGenerationInput(
            prompt = request.constructPrompt(templateResolver),
            maxContextLength = request.maxContextLength,
            maxLength = request.maxResponseLength,
            stopSequence = request.stopSequence,
            temperature = preset.temperature,
            repetitionPenalty = preset.repetitionPenalty,
            repetitionPenaltyRange = preset.repetitionPenaltyRange,
            repetitionPenaltySlope = preset.repetitionPenaltySlope,
            topP = preset.topP,
            topA = preset.topA,
            topK = preset.topK,
            typical = preset.typical,
            tailFreeSampling = preset.tailFreeSampling,
            minP = preset.minP,
            mirostat = preset.mirostat,
            mirostatTau = preset.mirostatTau,
            mirostatEta = preset.mirostatEta,
            samplerOrder = preset.samplerOrder,
            grammar = preset.grammar
        )
        val active = ActiveGeneration(UUID.randomUUID().toString(), CompletableDeferred())
        if (!activeGeneration.compareAndSet(null, active)) {
            emit(GenerationStatus.Error(GenerationError.Failure("A generation is already in progress.")))
            return@flow
        }

        try {
            emit(GenerationStatus.Started(active.id))
            emit(GenerationStatus.Generating)
            val result = coroutineScope {
                val generation = async {
                    repository.generate(preferences.baseUrl, input)
                }
                select {
                    generation.onAwait { it }
                    active.cancellation.onAwait {
                        generation.cancel()
                        null
                    }
                }
            }

            when {
                result == null -> emit(GenerationStatus.Aborted)
                result.isSuccess -> emit(GenerationStatus.Done(result.getOrThrow()))
                else -> emit(
                    GenerationStatus.Error(
                        GenerationError.Failure(
                            result.exceptionOrNull()?.localizedMessage
                                ?: "KoboldCpp generation failed."
                        )
                    )
                )
            }
        } finally {
            activeGeneration.compareAndSet(active, null)
        }
    }

    override suspend fun cancelGeneration(requestId: String): Boolean {
        val active = activeGeneration.get() ?: return false
        return active.id == requestId && active.cancellation.complete(Unit)
    }
}
