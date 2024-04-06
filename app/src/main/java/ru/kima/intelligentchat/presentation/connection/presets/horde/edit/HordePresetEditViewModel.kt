package ru.kima.intelligentchat.presentation.connection.presets.horde.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.domain.presets.kobold.model.KoboldPreset
import ru.kima.intelligentchat.domain.presets.kobold.useCase.GetKoboldPresetUseCase
import ru.kima.intelligentchat.domain.presets.kobold.useCase.UpdateKoboldPresetUseCase
import ru.kima.intelligentchat.presentation.connection.presets.horde.edit.events.UserEvent

@OptIn(FlowPreview::class)
class HordePresetEditViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val updateKoboldPreset: UpdateKoboldPresetUseCase,
    getKoboldPreset: GetKoboldPresetUseCase
) : ViewModel() {
    private val _preset = MutableStateFlow(KoboldPreset())
    val state = _preset.map {
        if (it.id == 0L) {
            HordePresetEditScreenState.NoPreset
        } else {
            HordePresetEditScreenState.Preset(it)
        }
    }

    init {
        val id = savedStateHandle.get<String>("presetId")?.toLongOrNull()
        id?.let {
            viewModelScope.launch {
                val preset = getKoboldPreset(it)
                    ?: return@launch
                _preset.value = preset

                _preset
                    .debounce(500)
                    .collect {
                        if (it.id > 0L) {
                            updateKoboldPreset(it)
                        }
                    }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        MainScope().launch(Dispatchers.IO) {
            if (_preset.value.id > 0L) {
                updateKoboldPreset(_preset.value)
            }
        }
    }

    fun onEvent(event: UserEvent) {
        when (event) {
            is UserEvent.EditTitle -> onEditTitle(event.title)
            is UserEvent.EditTemperature -> onEditTemperature(event.temperature)
            is UserEvent.EditTopK -> onEditTopK(event.topK)
            is UserEvent.EditTopP -> onEditTopP(event.topP)
            is UserEvent.EditTypical -> onEditTypical(event.typical)
            is UserEvent.EditMinP -> onEditMinP(event.minP)
            is UserEvent.EditTopA -> onEditTopA(event.topA)
            is UserEvent.EditTailFreeSampling -> onEditTailFreeSampling(event.tailFreeSampling)
            is UserEvent.EditRepetitionPenalty -> onEditRepetitionPenalty(event.repetitionPenalty)
            is UserEvent.EditRepetitionPenaltyRange -> onEditRepetitionPenaltyRange(event.repetitionPenaltyRange)
            is UserEvent.EditRepetitionPenaltySlope -> onEditRepetitionPenaltySlope(event.repetitionPenaltySlope)
            is UserEvent.EditMirostatMode -> onEditMirostatMode(event.mode)
            is UserEvent.EditMirostatTau -> onEditMirostatTau(event.tau)
            is UserEvent.EditMirostatEta -> onEditMirostatEta(event.eta)
            is UserEvent.StartMoveSampler -> onStartMoveSampler(event.startIndex, event.elementSize)
            is UserEvent.MoveSampler -> onMoveSampler(event.offset)
        }
    }

    private fun onEditTitle(newTitle: String) {
        val preset = _preset.value.copy(
            name = newTitle
        )
        _preset.value = preset
    }

    private fun onEditTemperature(temperature: Float) {
        val preset = _preset.value.copy(
            temperature = temperature
        )
        _preset.value = preset
    }

    private fun onEditTopK(topK: Int) {
        val preset = _preset.value.copy(
            topK = topK
        )
        _preset.value = preset
    }

    private fun onEditTopP(topP: Float) {
        val preset = _preset.value.copy(
            topP = topP
        )
        _preset.value = preset
    }

    private fun onEditTypical(typicalP: Float) {
        val preset = _preset.value.copy(
            typical = typicalP
        )
        _preset.value = preset
    }

    private fun onEditMinP(minP: Float) {
        val preset = _preset.value.copy(
            minP = minP
        )
        _preset.value = preset
    }

    private fun onEditTopA(topA: Float) {
        val preset = _preset.value.copy(
            topA = topA
        )
        _preset.value = preset
    }

    private fun onEditTailFreeSampling(tailFreeSampling: Float) {
        val preset = _preset.value.copy(
            tailFreeSampling = tailFreeSampling
        )
        _preset.value = preset
    }

    private fun onEditRepetitionPenalty(repetitionPenalty: Float) {
        val preset = _preset.value.copy(
            repetitionPenalty = repetitionPenalty
        )
        _preset.value = preset
    }

    private fun onEditRepetitionPenaltyRange(repetitionPenaltyRange: Int) {
        val preset = _preset.value.copy(
            repetitionPenaltyRange = repetitionPenaltyRange
        )
        _preset.value = preset
    }

    private fun onEditRepetitionPenaltySlope(repetitionPenaltySlope: Float) {
        val preset = _preset.value.copy(
            repetitionPenaltySlope = repetitionPenaltySlope
        )
        _preset.value = preset
    }

    private fun onEditMirostatMode(mode: Int) {
        val preset = _preset.value.copy(
            mirostat = mode
        )
        _preset.value = preset
    }

    private fun onEditMirostatTau(tau: Float) {
        val preset = _preset.value.copy(
            mirostatTau = tau
        )
        _preset.value = preset
    }

    private fun onEditMirostatEta(eta: Float) {
        val preset = _preset.value.copy(
            mirostatEta = eta
        )
        _preset.value = preset
    }

    private var _initialSamplerOrder = emptyList<Int>()
    private var _initialSamplerIndex = 0
    private var _elementSize = 150
    private var _indexOffset = 0

    private fun onStartMoveSampler(startIndex: Int, elementSize: Int) {
        _indexOffset = 0
        _initialSamplerIndex = startIndex
        _initialSamplerOrder = _preset.value.samplerOrder
        _elementSize = elementSize
    }

    private fun onMoveSampler(offset: Int) {
        val preset = _preset.value

        val verticalAdjustment = _elementSize / 2
        val currentIndexOffset = (offset + verticalAdjustment) / _elementSize
        if (currentIndexOffset == 0) return
        _indexOffset += currentIndexOffset
        var addIndex = _initialSamplerIndex + _indexOffset
        if (_indexOffset < 0) addIndex -= 1
        if (addIndex < 0) addIndex = 0
        if (addIndex > preset.samplerOrder.lastIndex) addIndex = preset.samplerOrder.lastIndex

        val result = if (_indexOffset != 0) {
            val temp = _initialSamplerOrder.toMutableList()
            val sampler = temp.removeAt(_initialSamplerIndex)
            temp.add(addIndex, sampler)
            temp
        } else {
            _initialSamplerOrder
        }
        _preset.value = preset.copy(
            samplerOrder = result
        )
    }
}