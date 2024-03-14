package ru.kima.intelligentchat.presentation.connection.overview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.kima.intelligentchat.common.ComposeEvent
import ru.kima.intelligentchat.core.common.API_TYPE
import ru.kima.intelligentchat.core.preferences.appPreferences.AppPreferences
import ru.kima.intelligentchat.core.preferences.hordeState.HordeState
import ru.kima.intelligentchat.core.preferences.hordeState.model.HordeModelInfo
import ru.kima.intelligentchat.domain.horde.useCase.GetKudosUseCase
import ru.kima.intelligentchat.domain.horde.useCase.LoadHordeModelsUseCase
import ru.kima.intelligentchat.domain.horde.useCase.SaveApiKeyUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.UpdateSelectedApiUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.GetHordePreferencesUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.SelectHordeModelsUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateContextToWorkerUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateGenerationDetailsUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateResponseToWorkerUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateTrustedWorkersUseCase
import ru.kima.intelligentchat.presentation.connection.overview.events.COUiEvent
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent
import ru.kima.intelligentchat.presentation.connection.overview.mappers.toDialogActiveModel
import ru.kima.intelligentchat.presentation.connection.overview.model.HordeDialogActiveModel

class ConnectionOverviewViewModel(
    private val savedStateHandle: SavedStateHandle,
    getPreferences: GetPreferencesUseCase,
    private val updateSelectedApi: UpdateSelectedApiUseCase,
    private val getHordePreferences: GetHordePreferencesUseCase,
    private val updateContextToWorker: UpdateContextToWorkerUseCase,
    private val updateResponseToWorker: UpdateResponseToWorkerUseCase,
    private val updateTrustedWorkers: UpdateTrustedWorkersUseCase,
    private val saveApiKey: SaveApiKeyUseCase,
    private val getKudos: GetKudosUseCase,
    private val loadActiveModels: LoadHordeModelsUseCase,
    private val selectHordeModels: SelectHordeModelsUseCase,
    private val updateGenerationDetails: UpdateGenerationDetailsUseCase,
) : ViewModel() {
    private val showApiToken = savedStateHandle.getStateFlow(SHOW_API_TOKEN_KEY, false)
    private val currentHordeApiToken =
        savedStateHandle.getStateFlow(HORDE_API_TOKEN_KEY, String())
    private val showSelectHordeModelsDialog = MutableStateFlow(false)
    private val hordeDialogActiveModels = MutableStateFlow(emptyList<HordeDialogActiveModel>())

    init {
        viewModelScope.launch {
            val preferences = getHordePreferences().first()
            savedStateHandle[HORDE_API_TOKEN_KEY] = preferences.apiToken
        }
    }

    @Suppress("UNCHECKED_CAST")
    val state = combine(
        getPreferences(),
        getHordePreferences(),
        currentHordeApiToken,
        showApiToken,
        showSelectHordeModelsDialog,
        hordeDialogActiveModels
    ) { args ->
        val preferences = args[0] as AppPreferences
        val hordeState = args[1] as HordeState
        val currentHordeApiToken = args[2] as String
        val showApiToken = args[3] as Boolean
        val showSelectHordeModelsDialog = args[4] as Boolean
        val hordeDialogActiveModels = args[5] as List<HordeDialogActiveModel>
        ConnectionOverviewState(
            selectedApiType = preferences.selectedApiType,
            hordeFragmentState = ConnectionOverviewState.HordeFragmentState(
                currentApiToken = currentHordeApiToken,
                showApiToken = showApiToken,
                contextToWorker = hordeState.contextToWorker,
                responseToWorker = hordeState.responseToWorker,
                trustedWorkers = hordeState.trustedWorkers,
                userName = hordeState.userName,
                contextSize = hordeState.contextSize,
                responseLength = hordeState.responseLength,
                showSelectHordeModelsDialog = showSelectHordeModelsDialog,
                selectedModels = hordeState.selectedModels,
                dialogSelectedModels = hordeDialogActiveModels
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ConnectionOverviewState())

    private val _uiEvents = MutableStateFlow(ComposeEvent<COUiEvent>(null))
    val uiEvents = _uiEvents.asStateFlow()

    fun onEvent(event: COUserEvent) {
        when (event) {
            is COUserEvent.UpdateSelectedApi -> onUpdateSelectedApi(event.apiType)
            COUserEvent.ToggleHordeTokenVisibility -> onToggleHordeTokenVisibility()
            is COUserEvent.UpdateApiToken -> onUpdateApiToken(event.token)
            COUserEvent.ToggleContextToWorker -> onToggleContextToWorker()
            COUserEvent.ToggleResponseToWorker -> onToggleResponseToWorker()
            COUserEvent.ToggleTrustedWorkers -> onToggleTrustedWorkers()
            COUserEvent.SaveApiKey -> onSaveApiKey()
            COUserEvent.ShowKudos -> onShowKudos()
            COUserEvent.RefreshModels -> onRefreshModels()
            COUserEvent.DismissSelectHordeModelsDialog -> onDismissSelectHordeModelsDialog()
            COUserEvent.OpenSelectHordeModelsDialog -> onOpenSelectHordeModelsDialog()
            is COUserEvent.CheckHordeModel -> onCheckHordeModel(event.model)
            COUserEvent.AcceptSelectHordeModelsDialog -> onAcceptSelectHordeModelsDialog()
            is COUserEvent.UpdateHordeContextSize -> onUpdateHordeContextSize(event.newSize)
            is COUserEvent.UpdateHordeResponseLength -> onUpdateHordeResponseLength(event.newLength)
        }
    }

    private fun onUpdateSelectedApi(apiType: API_TYPE) = viewModelScope.launch {
        updateSelectedApi(apiType)
    }

    private fun onToggleHordeTokenVisibility() {
        savedStateHandle[SHOW_API_TOKEN_KEY] = !showApiToken.value
    }

    private fun onUpdateApiToken(token: String) {
        savedStateHandle[HORDE_API_TOKEN_KEY] = token
    }

    private fun onToggleContextToWorker() = viewModelScope.launch {
        updateContextToWorker(!state.value.hordeFragmentState.contextToWorker)
    }

    private fun onToggleResponseToWorker() = viewModelScope.launch {
        updateResponseToWorker(!state.value.hordeFragmentState.responseToWorker)
    }

    private fun onToggleTrustedWorkers() = viewModelScope.launch {
        updateTrustedWorkers(!state.value.hordeFragmentState.trustedWorkers)
    }

    private fun onSaveApiKey() = viewModelScope.launch {
        val apiKey = state.value.hordeFragmentState.currentApiToken

        val event = when (val result = saveApiKey(apiKey)) {
            is SaveApiKeyUseCase.SaveApiKeyResult.Success -> COUiEvent.ShowSnackbar(COUiEvent.COSnackbar.ApiKeySaved)
            SaveApiKeyUseCase.SaveApiKeyResult.UserNotFound -> COUiEvent.ShowSnackbar(COUiEvent.COSnackbar.HordeUserNotFound)
            SaveApiKeyUseCase.SaveApiKeyResult.ValidationError -> COUiEvent.ShowSnackbar(COUiEvent.COSnackbar.HordeValidationError)
            SaveApiKeyUseCase.SaveApiKeyResult.NoInternet -> COUiEvent.ShowSnackbar(COUiEvent.COSnackbar.NoInternet)
            SaveApiKeyUseCase.SaveApiKeyResult.EmtpyKey -> COUiEvent.ShowSnackbar(COUiEvent.COSnackbar.EmptyHordeKey)
            is SaveApiKeyUseCase.SaveApiKeyResult.UnknownError -> COUiEvent.ShowSnackbar(
                COUiEvent.COSnackbar.HordeUnknownError(
                    result.message
                )
            )
        }

        _uiEvents.value = ComposeEvent(event)
    }

    private fun onShowKudos() = viewModelScope.launch {
        val event = when (val result = getKudos()) {
            GetKudosUseCase.GetKudosResult.NoUser -> COUiEvent.ShowSnackbar(COUiEvent.COSnackbar.NoUser)
            GetKudosUseCase.GetKudosResult.NoInternet -> COUiEvent.ShowSnackbar(COUiEvent.COSnackbar.NoInternet)
            is GetKudosUseCase.GetKudosResult.UnknownError -> COUiEvent.ShowSnackbar(
                COUiEvent.COSnackbar.HordeUnknownError(
                    result.message
                )
            )

            is GetKudosUseCase.GetKudosResult.Success -> COUiEvent.ShowSnackbar(
                COUiEvent.COSnackbar.ShowKudos(
                    result.kudos
                )
            )
        }

        _uiEvents.value = ComposeEvent(event)
    }

    private fun onRefreshModels() = viewModelScope.launch {
        val event = when (val result = loadActiveModels()) {
            LoadHordeModelsUseCase.LoadHordeModelsResult.NoInternet -> COUiEvent.ShowSnackbar(
                COUiEvent.COSnackbar.NoInternet
            )

            is LoadHordeModelsUseCase.LoadHordeModelsResult.Success -> COUiEvent.ShowSnackbar(
                COUiEvent.COSnackbar.ModelsUpdated(result.modelsCount)
            )

            is LoadHordeModelsUseCase.LoadHordeModelsResult.UnknownError -> COUiEvent.ShowSnackbar(
                COUiEvent.COSnackbar.HordeUnknownError(result.message)
            )
        }

        _uiEvents.value = ComposeEvent(event)
    }

    private fun onDismissSelectHordeModelsDialog() {
        showSelectHordeModelsDialog.value = false
    }

    private fun onOpenSelectHordeModelsDialog() = viewModelScope.launch {
        val modelsInfo = getHordePreferences().first().modelsInfo
        hordeDialogActiveModels.value =
            getDialogActiveModes(
                modelsInfo,
                state.value.hordeFragmentState.selectedModels
            )
        showSelectHordeModelsDialog.value = true
    }

    private fun onCheckHordeModel(model: String) {
        val list = hordeDialogActiveModels.value.toMutableList()

        for (i in list.indices) {
            if (list[i].name != model) {
                continue
            }

            val selected = list[i].selected
            list[i] = list[i].copy(selected = !selected)
            break
        }
        hordeDialogActiveModels.value = list
    }

    private fun onAcceptSelectHordeModelsDialog() = viewModelScope.launch {
        val selectedModels = hordeDialogActiveModels.value
            .asSequence()
            .filter { it.selected }
            .map { it.name }
            .toList()

        selectHordeModels(selectedModels)
        showSelectHordeModelsDialog.value = false
    }

    private fun onUpdateHordeContextSize(newSize: Float) = viewModelScope.launch {
        updateGenerationDetails(contextSize = newSize.toInt())
    }

    private fun onUpdateHordeResponseLength(newLength: Float) = viewModelScope.launch {
        updateGenerationDetails(responseLength = newLength.toInt())
    }

    companion object {
        private const val SHOW_API_TOKEN_KEY = "showApiToken"
        private const val HORDE_API_TOKEN_KEY = "currentHordeApiToken"

        private fun getDialogActiveModes(
            activeModels: List<HordeModelInfo>,
            selectedModels: List<String>
        ): List<HordeDialogActiveModel> {
            val selectedModelsOnline = activeModels
                .map { it.name }
                .intersect(selectedModels.toSet())

            val result = activeModels.map {
                it.toDialogActiveModel(selectedModelsOnline.contains(it.name))
            }
            return result
        }
    }
}