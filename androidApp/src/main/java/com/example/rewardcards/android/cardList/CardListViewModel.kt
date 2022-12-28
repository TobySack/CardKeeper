package com.example.rewardcards.android.cardList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rewardcards.domain.card.Card
import com.example.rewardcards.domain.card.CardDataSource
import com.example.rewardcards.domain.card.SearchCards
import com.example.rewardcards.domain.time.DateTimeUtil
import com.example.rewardcards.presentation.BabyBlueHex
import com.example.rewardcards.presentation.RedOrangeHex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardListViewModel @Inject constructor(
    private val cardDataSource: CardDataSource,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val searchCards = SearchCards()

    private val cards = savedStateHandle.getStateFlow("cards", emptyList<Card>())
    private val searchText = savedStateHandle.getStateFlow("searchText", "")
    private val isSearchActive = savedStateHandle.getStateFlow("isSearchActive", false)

    val state = combine(cards, searchText, isSearchActive) { cards, searchText, isSearchActive ->
        CardListState(
            cards = searchCards.execute(cards, searchText),
            searchCards = searchText,
            isSearchActive = isSearchActive
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CardListState())

    init {
        viewModelScope.launch {
            if (cardDataSource.getAllCards().isEmpty()) {
                (1..2).forEach {
                    cardDataSource.insertCard(
                        Card(
                            id = null,
                            name = "McDonalds",
                            image = "null",
                            barcode = "69010140244368707",
                            color = RedOrangeHex,
                            created = DateTimeUtil.now(),
                            notes = BabyBlueHex.toString()
                        )
                    )
                }
            }
        }
    }

    fun loadCards() {
        viewModelScope.launch {
            savedStateHandle["cards"] = cardDataSource.getAllCards()
        }
    }

    fun onSearchTextChange(text: String) {
        savedStateHandle["searchText"] = text
    }

    fun onToggleSearch() {
        savedStateHandle["isSearchActive"] = !isSearchActive.value

        if (!isSearchActive.value) {
            savedStateHandle["searchText"] = ""
        }
    }

    fun deleteCardById(id: Long) {
        viewModelScope.launch {
            cardDataSource.deleteCardById(id)
            loadCards()
        }
    }
}