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
import ru.kima.intelligentchat.domain.horde.useCase.GetActiveModelsUseCase
import ru.kima.intelligentchat.domain.horde.useCase.GetKudosUseCase
import ru.kima.intelligentchat.domain.horde.useCase.SaveApiKeyUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.GetPreferencesUseCase
import ru.kima.intelligentchat.domain.preferences.app.useCase.UpdateSelectedApiUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.GetHordePreferencesUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateContextToWorkerUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateResponseToWorkerUseCase
import ru.kima.intelligentchat.domain.preferences.horde.useCase.UpdateTrustedWorkersUseCase
import ru.kima.intelligentchat.presentation.connection.overview.events.COUiEvent
import ru.kima.intelligentchat.presentation.connection.overview.events.COUserEvent

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
    private val getActiveModels: GetActiveModelsUseCase
) : ViewModel() {
    private val showApiToken = savedStateHandle.getStateFlow(SHOW_API_TOKEN_KEY, false)
    private val currentHordeApiToken =
        savedStateHandle.getStateFlow(HORDE_API_TOKEN_KEY, String())

    init {
        viewModelScope.launch {
            val preferences = getHordePreferences().first()
            savedStateHandle[HORDE_API_TOKEN_KEY] = preferences.apiToken
        }
    }

    val state = combine(
        getPreferences(),
        getHordePreferences(),
        currentHordeApiToken,
        showApiToken
    ) { preferences, hordePreferences, currentHordeApiToken, showApiToken ->
        ConnectionOverviewState(
            selectedApiType = preferences.selectedApiType,
            hordeFragmentState = ConnectionOverviewState.HordeFragmentState(
                currentApiToken = currentHordeApiToken,
                showApiToken = showApiToken,
                contextToWorker = hordePreferences.contextToWorker,
                responseToWorker = hordePreferences.responseToWorker,
                trustedWorkers = hordePreferences.trustedWorkers,
                userName = hordePreferences.userName,
                contextSize = hordePreferences.contextSize,
                responseLength = hordePreferences.responseLength
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
        val event = when (val result = getActiveModels()) {
            GetActiveModelsUseCase.GetActiveModelsResult.NoInternet -> COUiEvent.ShowSnackbar(
                COUiEvent.COSnackbar.NoInternet
            )

            is GetActiveModelsUseCase.GetActiveModelsResult.Success -> COUiEvent.ShowMessage(result.models.map { it.name }
                .toString())

            is GetActiveModelsUseCase.GetActiveModelsResult.UnknownError -> COUiEvent.ShowSnackbar(
                COUiEvent.COSnackbar.HordeUnknownError(result.message)
            )
        }

        _uiEvents.value = ComposeEvent(event)
    }

    companion object {
        private const val SHOW_API_TOKEN_KEY = "showApiToken"
        private const val HORDE_API_TOKEN_KEY = "currentHordeApiToken"
    }
}