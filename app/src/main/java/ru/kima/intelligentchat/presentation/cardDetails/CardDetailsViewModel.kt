package ru.kima.intelligentchat.presentation.cardDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kima.intelligentchat.common.Resource
import ru.kima.intelligentchat.domain.useCase.characterCard.GetCardUseCase

class CardDetailsViewModel : ViewModel(), KoinComponent {
    private val _state = MutableStateFlow(CardDetailsState())
    val state = _state.asStateFlow()

    private val getCard: GetCardUseCase by inject()

    fun loadCard(id: Int) {
        getCard(id).onEach { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            card = resource.data!!
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}