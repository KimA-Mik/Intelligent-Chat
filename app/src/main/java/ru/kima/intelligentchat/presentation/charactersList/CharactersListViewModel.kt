package ru.kima.intelligentchat.presentation.charactersList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kima.intelligentchat.common.Resource
import ru.kima.intelligentchat.domain.model.CharacterCard
import ru.kima.intelligentchat.domain.useCase.characterCard.GetCardsUseCase
import ru.kima.intelligentchat.domain.useCase.characterCard.PutCardUseCase

open class CharactersListViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), KoinComponent {
    private val _state = MutableStateFlow(CharactersListState())
    val state = _state.asStateFlow()

    private val getCards: GetCardsUseCase by inject()
    private val putCards: PutCardUseCase by inject()

    init {
        loadCards()
    }

    private fun loadCards() = viewModelScope.launch {
        getCards().collect { result ->
            if (result is Resource.Success) {
                _state.update {
                    it.copy(
                        cards = result.data!!
                    )
                }

                if (result.data!!.isEmpty()) {
                    repeat(100) { index ->
                        val i = index + 1
                        val card =
                            CharacterCard(id = i, name = "Name $i", description = "Descriptione $i")
                        putCards(card)
                    }
                }
            }
        }
    }
}