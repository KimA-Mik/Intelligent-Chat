package ru.kima.intelligentchat.presentation.cardDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kima.intelligentchat.common.Resource
import ru.kima.intelligentchat.domain.model.CharacterCard
import ru.kima.intelligentchat.domain.useCase.characterCard.GetCardUseCase

class CardDetailsViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel(),
    KoinComponent {
    private val cardTitle = savedStateHandle.getStateFlow("cardTitle", "Title")
    private val cardDescription = savedStateHandle.getStateFlow("cardDescription", "Description")

    val state = combine(cardTitle, cardDescription) { cardTitle, cardDescription ->
        CardDetailsState(card = CharacterCard(name = cardTitle, description = cardDescription))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CardDetailsState())

    private val getCard: GetCardUseCase by inject()

    init {
        savedStateHandle.get<Int>("cardId")?.let {
            loadCard(it)
        }
    }

    fun loadCard(id: Int) {
        getCard(id).onEach { resource ->
            when (resource) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val card = resource.data!!
                    savedStateHandle["cardTitle"] = card.name
                    savedStateHandle["cardDescription"] = card.description
                }
            }
        }.launchIn(viewModelScope)
    }
}