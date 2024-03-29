package ru.kima.intelligentchat.presentation.connection.presets.horde.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
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


    init {
        val id = savedStateHandle.get<Long>("presetId")
        id?.let {
            viewModelScope.launch {
                val preset = getKoboldPreset(it)
                    ?: return@launch
                _preset.value = preset

                _preset
                    .debounce(500)
                    .collect { savedPreset ->
                        updateKoboldPreset(savedPreset)
                    }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        MainScope().launch(Dispatchers.IO) {
            updateKoboldPreset(_preset.value)
        }
    }

    fun onEvent(event: UserEvent) {

    }
}